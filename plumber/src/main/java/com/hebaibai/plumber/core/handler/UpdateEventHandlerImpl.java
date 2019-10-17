package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.EventHandler;
import com.hebaibai.plumber.core.SqlEventData;
import com.hebaibai.plumber.core.EventDataExecuter;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

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
        Map<String, String> eventBeforData = new HashMap<>();
        Map<String, String> eventAfterData = new HashMap<>();

        for (int i = 0; i < columns.size(); i++) {
            String sourceName = columns.get(i);
            String targetName = mapping.get(sourceName);
            if (targetName == null) {
                continue;
            }
            //设置更新前的数据
            eventBeforData.put(targetName, befor[i]);
            //设置更新后的数据
            eventAfterData.put(targetName, after[i]);
        }
        //填充插件数据
        SqlEventData eventPluginData = new SqlEventData(SqlEventData.TYPE_UPDATE);
        //添加变动前的数据
        eventPluginData.setBefor(eventBeforData);
        //添加变动后的数据
        eventPluginData.setAfter(eventAfterData);
        eventPluginData.setSourceDatabase(this.sourceDatabase);
        eventPluginData.setSourceTable(this.sourceTable);
        eventPluginData.setTargetDatabase(this.targetDatabase);
        eventPluginData.setTargetTable(this.targetTable);
        eventPluginData.setKey(mapping.get(this.key));
        for (EventDataExecuter eventPlugin : eventPlugins) {
            try {
                eventPlugin.execute(eventPluginData);
            } catch (Exception e) {
                log.error(eventPlugin.getClass().getName(), e);
            }
        }
    }

    @Override
    public String getName() {
        return "update event hander";
    }
}
