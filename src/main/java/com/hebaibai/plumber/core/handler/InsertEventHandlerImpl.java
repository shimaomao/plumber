package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 插入事件处理器
 *
 * @author hjx
 */
@Slf4j
public class InsertEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    @Override
    public boolean support(EventType eventType, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isWrite(eventType)) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public Runnable handle(EventData data) {
        log.info("new event insert ... ");
        String[] rows = EventDataUtils.getInsertRows(data);
        List<String> columns = sourceTableMateData.getColumns();
        List<String> targetColumns = new ArrayList<>();
        List<String> targetColumnValues = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            if (!mapping.containsKey(sourceName)) {
                continue;
            }
            //目标字段
            String targetName = mapping.get(sourceName);
            targetColumns.add("`" + targetName + "`");
            if (rows[i] == null) {
                targetColumnValues.add("null");
            } else {
                targetColumnValues.add("'" + rows[i] + "'");
            }
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("REPLACE INTO ");
        sqlBuilder.append(targetDatabase).append(".").append(targetTable);
        sqlBuilder.append(" ( ").append(String.join(", ", targetColumns));
        sqlBuilder.append(" ) VALUES ( ").append(String.join(", ", targetColumnValues));
        sqlBuilder.append(");");
        String sql = sqlBuilder.toString();
        return sqlRunnable(sql);
    }


    @Override
    public String getName() {
        return "insert event hander";
    }
}
