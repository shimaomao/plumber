package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    public DeleteEventHandlerImpl() {
    }

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
    public void handle(EventData data) {

    }

    @Override
    public String getName() {
        return name;
    }
}
