package com.hebaibai.plumber.core;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 监听数据库的binlog事件
 *
 * @author hjx
 */
@Slf4j
public class BinlogEventListener implements BinaryLogClient.EventListener {

    private EventBus eventBus;

    private Set<EventHandler> eventHandlers;

    private TableIdMapping tableIdMapping = new TableIdMapping();

    public BinlogEventListener(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * 添加EventData处理器
     *
     * @param eventHandlers
     */
    public void setEventHandlers(Set<EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    /**
     * 实现监听事件
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        EventHeader header = event.getHeader();
        EventType eventType = header.getEventType();
        EventData data = event.getData();
        if (EventType.TABLE_MAP == eventType) {
            TableMapEventData tableMapEventData = EventDataUtils.getTableMapEventData(data);
            long tableId = tableMapEventData.getTableId();
            String tableName = tableMapEventData.getTable();
            String databaseName = tableMapEventData.getDatabase();
            tableIdMapping.seveTableName(tableId, tableName);
            tableIdMapping.seveDatabaseName(tableId, databaseName);
            return;
        }
        if (!EventType.isRowMutation(eventType)) {
            return;
        }

        log.debug("binlog event: {}", event);

        if (eventHandlers == null) {
            return;
        }
        Long tableId = EventDataUtils.getTableId(data);
        String tableName = tableIdMapping.getTableName(tableId);
        String databaseName = tableIdMapping.getDatabaseName(tableId);
        for (EventHandler handle : eventHandlers) {
            boolean support = handle.support(eventType, databaseName, tableName);
            if (!support) {
                continue;
            }
            handle.handle(eventBus, data);
        }
    }

}
