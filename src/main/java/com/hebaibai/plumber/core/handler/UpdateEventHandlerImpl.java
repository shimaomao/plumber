package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.Auth;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 更新事件处理器
 *
 * @author hjx
 */
@Slf4j
public class UpdateEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    @Override
    public boolean support(EventType eventType, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isUpdate(eventType)) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public Runnable handle(EventData data) {

        log.info("new event update ... ");
        String[] befor = EventDataUtils.getBeforUpdate(data);
        String[] after = EventDataUtils.getAfterUpdate(data);
        //拼装sql需要的数据
        List<String> columns = sourceTableMateData.getColumns();
        List<String> updateColumns = new ArrayList<>();
        List<String> updateKeyColumns = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            //是否是key
            boolean isKey = keys.contains(sourceName) && mapping.containsKey(sourceName);
            //不是key或者数据没有变化的，跳过
            if (!isKey && Objects.equals(befor[i], after[i])) {
                continue;
            }
            String targetName = mapping.get(sourceName);
            //如果是key，以key为条件执行更新
            if (isKey) {
                updateKeyColumns.add("`" + targetName + "` = '" + befor[i] + "'");
            }
            if (after[i] == null) {
                updateColumns.add("`" + targetName + "` = null");
            } else {
                updateColumns.add("`" + targetName + "` = '" + after[i] + "'");
            }
        }

        if (updateColumns.size() == 0) {
            return null;
        }

        //拼装sql
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE ").append(targetDatabase).append(".").append(targetTable).append(" SET ");
        sqlBuilder.append(String.join(", ", updateColumns));
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(String.join("AND ", updateKeyColumns));
        String sql = sqlBuilder.toString();
        return sqlRunnable(sql);
    }

    @Override
    public String getName() {
        return "update event hander";
    }
}
