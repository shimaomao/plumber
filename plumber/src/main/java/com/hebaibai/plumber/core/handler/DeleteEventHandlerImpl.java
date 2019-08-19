package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.ConsumerAddress;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除事件处理器
 *
 * @author hjx
 */
public class DeleteEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    @Override
    public boolean support(EventHeader eventHeader, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isDelete(eventHeader.getEventType())) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public void handle(EventBus eventBus, EventData data) {
        log.info("new event delete ... ");
        String[] rows = EventDataUtils.getDeleteRows(data);
        List<String> columns = sourceTableMateData.getColumns();
        List<String> wheres = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            boolean isKey = keys.contains(sourceName) && mapping.containsKey(sourceName);
            if (!isKey) {
                continue;
            }
            String targetName = mapping.get(sourceName);
            if (rows[i] == null) {
                wheres.add(targetName + " = null ");
            } else {
                wheres.add("`" + targetName + "`" + " = '" + rows[i] + "' ");
            }
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM ");
        sqlBuilder.append(targetDatabase).append(".").append(targetTable);
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(String.join("and ", wheres));
        String sql = sqlBuilder.toString();
        eventBus.send(ConsumerAddress.EXECUTE_SQL_DELETE, sql);
    }

    @Override
    public String getName() {
        return "delete event hander";
    }
}
