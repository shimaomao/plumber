package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class UpdateEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    public UpdateEventHandlerImpl() {
    }

    @Override
    public boolean support(EventType eventType, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isUpdate(eventType)) {
            return false;
        }
        return database.equals(dataBaseName) && table.equals(tableName);
    }

    @Override
    public void handle(EventData data) {
        log.info("new event ... ");
        String[] befor = EventDataUtils.getBeforUpdate(data);
        String[] after = EventDataUtils.getAfterUpdate(data);
        //拼装sql需要的数据
        List<String> targetColumns = new ArrayList<>();
        List<String> targetColumnValues = new ArrayList<>();
        List<String> columns = tableMateData.getColumns();

        Map<String, String> keyColumnMap = targetTable.getKeyColumnMap();
        for (int i = 0; i < columns.size(); i++) {
            String fromName = columns.get(i);
            //是否是key
            boolean isKey = keyColumnMap.containsKey(fromName);
            //不是key或者数据没有变化的，跳过
            if (!isKey && Objects.equals(befor[i], after[i])) {
                continue;
            }
            String targetName = targetTable.getColumnMap().get(fromName);
            targetColumns.add("`" + targetName + "`");
            if (after[i] == null) {
                after[i] = "null";
            } else {
                targetColumnValues.add("'" + after[i] + "'");
            }
        }
        //拼装sql
        StringBuilder sql = new StringBuilder();
        sql.append("REPLACE INTO ");
        sql.append(targetTable.getDatabase()).append(".").append(targetTable.getTable());
        sql.append(" ( ").append(String.join(", ", targetColumns));
        sql.append(" ) VALUES ( ").append(String.join(", ", targetColumnValues));
        sql.append(");");
        System.out.println(sql.toString());
    }

    @Override
    public String getName() {
        return name;
    }
}
