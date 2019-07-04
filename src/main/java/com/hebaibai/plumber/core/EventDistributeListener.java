package com.hebaibai.plumber.core;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * 监听数据库的binlog事件
 *
 * @author hjx
 */
@Slf4j
class EventDistributeListener implements BinaryLogClient.EventListener {

    private ExecutorService executorService;

    private Auth auth;

    @Getter
    private Set<EventHandler> eventHandlers = new HashSet<>();


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
        String tableName = auth.getTableName(tableId);
        String databaseName = auth.getDatabaseName(tableId);
        for (EventHandler handle : eventHandlers) {
            boolean support = handle.support(eventType, databaseName, tableName);
            if (support) {
                handle.handle(data);
            }
        }
    }


    public EventDistributeListener(Auth auth, ExecutorService executorService) {
        this.executorService = executorService;
        this.auth = auth;
    }
}
