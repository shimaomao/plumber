package com.hebaibai.plumber.core.executer;

import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.ConsumerAddress;
import com.hebaibai.plumber.core.SqlEventData;
import com.hebaibai.plumber.core.SqlEventDataExecuter;
import io.vertx.core.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 执行 mysql
 *
 * @author hjx
 */
public class MysqlEventExecuter implements SqlEventDataExecuter {
    @Override
    public void setConfig(JSONObject config) {

    }

    @Override
    public void execute(EventBus eventBus, SqlEventData sqlEventData) throws Exception {
        String type = sqlEventData.getType();
        //拼装sql
        StringBuilder sqlBuilder = new StringBuilder();
        List<String> wheres = new ArrayList<>();
        switch (type) {
            //insert
            case SqlEventData.TYPE_INSERT:
                List<String> columns = new ArrayList<>();
                List<String> columnValues = new ArrayList<>();
                for (Map.Entry<String, String> entry : sqlEventData.getAfter().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    columns.add("`" + key + "`");
                    if (value == null) {
                        columnValues.add("null");
                    } else {
                        columnValues.add("'" + value + "'");
                    }
                }
                sqlBuilder.append("REPLACE INTO ");
                sqlBuilder.append(sqlEventData.getTargetDatabase()).append(".").append(sqlEventData.getTargetTable());
                sqlBuilder.append(" ( ").append(String.join(", ", columns));
                sqlBuilder.append(" ) VALUES ( ").append(String.join(", ", columnValues));
                sqlBuilder.append(");");
                eventBus.send(ConsumerAddress.EXECUTE_SQL_INSERT, sqlBuilder.toString());
                break;

            //delete
            case SqlEventData.TYPE_DELETE:
                for (Map.Entry<String, String> entry : sqlEventData.getAfter().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //是否是key
                    boolean isKey = sqlEventData.getKey().equals(key);
                    if (!isKey) {
                        continue;
                    }
                    if (value == null) {
                        wheres.add(key + " = null ");
                    } else {
                        wheres.add("`" + key + "`" + " = '" + value + "' ");
                    }
                }
                sqlBuilder.append("DELETE FROM ");
                sqlBuilder.append(sqlEventData.getTargetDatabase()).append(".").append(sqlEventData.getTargetTable());
                sqlBuilder.append(" WHERE ");
                sqlBuilder.append(String.join("and ", wheres));
                eventBus.send(ConsumerAddress.EXECUTE_SQL_DELETE, sqlBuilder.toString());
                break;

            //update
            case SqlEventData.TYPE_UPDATE:
                List<String> updates = new ArrayList<>();
                for (Map.Entry<String, String> entry : sqlEventData.getAfter().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    //变动之前的值
                    String beforValue = sqlEventData.getBefor().get(key);
                    //是否是key
                    boolean isKey = sqlEventData.getKey().equals(key);
                    //不是key或者数据没有变化的，跳过
                    if (!isKey && Objects.equals(value, beforValue)) {
                        continue;
                    }
                    //如果是key，以key为条件执行更新
                    if (isKey) {
                        wheres.add("`" + key + "` = '" + beforValue + "'");
                    }
                    if (value == null) {
                        updates.add("`" + key + "` = null");
                    } else {
                        updates.add("`" + key + "` = '" + value + "'");
                    }
                }
                // 没有更新项, 跳过
                if (updates.size() == 0) {
                    break;
                }
                sqlBuilder.append("UPDATE ")
                        .append(sqlEventData.getTargetDatabase()).append(".").append(sqlEventData.getTargetTable())
                        .append(" SET ");
                sqlBuilder.append(String.join(", ", updates));
                sqlBuilder.append(" WHERE ");
                sqlBuilder.append(String.join("AND ", wheres));
                eventBus.send(ConsumerAddress.EXECUTE_SQL_UPDATE, sqlBuilder.toString());
                break;

            //不做处理
            default:
                break;
        }
    }

}
