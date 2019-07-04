package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
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
        List<String> columns = super.tableMateData.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (!Objects.equals(befor[i], after[i])) {
                log.info("name:{}, value:{}", columns.get(i), after[i]);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
