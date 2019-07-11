package com.hebaibai.plumber.core;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
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

    private DataSourceConfig dataSourceConfig;

    private EventBus eventBus;

    private Set<EventHandler> eventHandlers;

    public BinlogEventListener(DataSourceConfig dataSourceConfig, EventBus eventBus) {
        this.dataSourceConfig = dataSourceConfig;
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
        boolean trueType = EventType.TABLE_MAP == eventType || EventType.isRowMutation(eventType);
        if (!trueType) {
            return;
        }
        log.debug("binlog event: {}", event);
        Long tableId = EventDataUtils.getTableId(data);
        String tableName = dataSourceConfig.getTableName(tableId);
        String databaseName = dataSourceConfig.getDatabaseName(tableId);
        for (EventHandler handle : eventHandlers) {
            boolean support = handle.support(eventType, databaseName, tableName);
            if (!support) {
                continue;
            }
            handle.handle(eventBus, data);
        }
    }

}
