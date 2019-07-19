package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import com.hebaibai.plumber.core.utils.TableMateData;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

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
    public void setSource(TableMateData tableMateData) {
        super.setSource(tableMateData);
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.setSource(tableMateData);
        }
    }

    @Override
    public void setTarget(TableMateData tableMateData) {
        super.setTarget(tableMateData);
        for (AbstractEventHandler eventHandler : eventHandlers) {
            eventHandler.setTarget(tableMateData);
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
    public boolean support(EventHeader eventHeader, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isRowMutation(eventHeader.getEventType())) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public void handle(EventBus eventBus, EventData data) {
        //插入事件
        if (EventDataUtils.getWriteRowsEventData(data) != null) {
            this.eventHandlers.get(0).handle(eventBus, data);
            return;
        }
        //更新事件
        else if (EventDataUtils.getUpdateRowsEventData(data) != null) {
            this.eventHandlers.get(1).handle(eventBus, data);
            return;
        }
        //删除事件
        else if (EventDataUtils.getDeleteRowsEventData(data) != null) {
            this.eventHandlers.get(2).handle(eventBus, data);
            return;
        }
        log.error("不支持的操作");
    }
}
