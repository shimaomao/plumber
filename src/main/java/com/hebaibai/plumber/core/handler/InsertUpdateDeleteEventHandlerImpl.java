package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.Auth;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 增删改事件综合和处理器
 *
 * @author hjx
 */
@Slf4j
public class InsertUpdateDeleteEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    private List<AbstractEventHandler> eventHandlers = Arrays.asList(
            //插入
            new InsertEventHandlerImpl(),
            //更新
            new UpdateEventHandlerImpl(),
            //删除
            new DeleteEventHandlerImpl()
    );

    public InsertUpdateDeleteEventHandlerImpl() {
    }

    @Override
    public String getName() {
        return "insert update delete event hander";
    }

    @Override
    public void setSource(Auth auth, String database, String table) throws SQLException {
        super.setSource(auth, database, table);
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.sourceDatabase = database;
            eventHandler.sourceTable = table;
            eventHandler.auth = auth;
            eventHandler.sourceTableMateData = this.sourceTableMateData;
        }
    }

    @Override
    public void setTarget(DataSource dataSource, String database, String table) throws SQLException {
        super.setTarget(dataSource, database, table);
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.targetDataSource = dataSource;
            eventHandler.targetDatabase = database;
            eventHandler.targetTable = table;
            eventHandler.targetTableMateData = this.targetTableMateData;
        }
    }

    @Override
    public void setStatus(boolean isRun) {
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.setStatus(isRun);
        }
    }

    @Override
    public void setMapping(Map<String, String> mapping) {
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.mapping = mapping;
        }
    }

    @Override
    public void setKeys(Set<String> keys) {
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.keys = keys;
        }
    }

    @Override
    public boolean support(EventType eventType, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isRowMutation(eventType)) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public void handle(EventData data) {
        //插入事件
        if (EventDataUtils.getWriteRowsEventData(data) != null) {
            this.eventHandlers.get(0).handle(data);
            return;
        }
        //更新事件
        else if (EventDataUtils.getUpdateRowsEventData(data) != null) {
            this.eventHandlers.get(1).handle(data);
            return;
        }
        //删除事件
        else if (EventDataUtils.getDeleteRowsEventData(data) != null) {
            this.eventHandlers.get(2).handle(data);
            return;
        }

        log.error("不支持的操作");
    }
}
