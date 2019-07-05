package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public void handle(EventData data) {
        String[] rows = EventDataUtils.getRows(data);
        List<String> columns = sourceTableMateData.getColumns();
        List<String> targetColumns = new ArrayList<>();
        List<String> targetColumnValues = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            if (!mapping.containsKey(sourceName)) {
                continue;
            }
            //目标字段
            targetColumns.add(mapping.get(sourceName));
            if (rows == null) {
                targetColumnValues.add("null");
            } else {
                //TODO: 需要添加数据转换
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
        log.info(sql);
    }


    @Override
    public String getName() {
        return name;
    }
}
