package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.ConsumerAddress;
import com.hebaibai.plumber.core.handler.plugin.EventPlugin;
import com.hebaibai.plumber.core.handler.plugin.EventPluginData;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import sun.nio.cs.ext.MacArabic;

import java.util.*;

/**
 * 更新事件处理器
 *
 * @author hjx
 */
@Slf4j
public class UpdateEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    @Override
    public boolean support(EventHeader eventHeader, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        if (!EventType.isUpdate(eventHeader.getEventType())) {
            return false;
        }
        return sourceDatabase.equals(dataBaseName) && sourceTable.equals(tableName);
    }

    @Override
    public void handle(EventBus eventBus, EventData data) {
        String[] befor = EventDataUtils.getBeforUpdate(data);
        String[] after = EventDataUtils.getAfterUpdate(data);
        //拼装sql需要的数据
        List<String> columns = sourceTableMateData.getColumns();
        List<String> updateColumns = new ArrayList<>();
        List<String> updateKeyColumns = new ArrayList<>();
        Map<String, String> eventAfterData = new HashMap<>();

        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            //是否是key
            boolean isKey = keys.contains(sourceName) && mapping.containsKey(sourceName);
            //不是key或者数据没有变化的，跳过
            if (!isKey && Objects.equals(befor[i], after[i])) {
                continue;
            }
            String targetName = mapping.get(sourceName);
            if (targetName == null) {
                continue;
            }
            //如果是key，以key为条件执行更新
            if (isKey) {
                updateKeyColumns.add("`" + targetName + "` = '" + befor[i] + "'");
            }
            if (after[i] == null) {
                updateColumns.add("`" + targetName + "` = null");
            } else {
                updateColumns.add("`" + targetName + "` = '" + after[i] + "'");
            }
            //设置更新后的数据
            eventAfterData.put(targetName, after[i]);
        }
        if (updateColumns.size() == 0) {
            return;
        }
        //填充插件数据
        EventPluginData eventPluginData = new EventPluginData();
        eventPluginData.setAfterData(eventAfterData);
        eventPluginData.setSourceDatabase(this.sourceDatabase);
        eventPluginData.setSourceTable(this.sourceTable);
        eventPluginData.setTargetDatabase(this.targetDatabase);
        eventPluginData.setTargetTable(this.targetTable);
        eventPluginData.setKeys(this.keys);
        for (EventPlugin eventPlugin : eventPlugins) {
            try {
                eventPlugin.doWithPlugin(EventPlugin.TYPE_UPDATE, eventPluginData);
            } catch (Exception e) {
                log.error(eventPlugin.getClass().getName(), e);
            }
        }
        //拼装sql
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE ").append(targetDatabase).append(".").append(targetTable).append(" SET ");
        sqlBuilder.append(String.join(", ", updateColumns));
        sqlBuilder.append(" WHERE ");
        sqlBuilder.append(String.join("AND ", updateKeyColumns));
        String sql = sqlBuilder.toString();
        eventBus.send(ConsumerAddress.EXECUTE_SQL_UPDATE, sql);
    }

    @Override
    public String getName() {
        return "update event hander";
    }
}
