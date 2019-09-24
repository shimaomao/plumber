package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.utils.EventDataUtils;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 打印事件
 *
 * @author hjx
 */
@Slf4j
public class PrintEventHandlerImpl extends AbstractEventHandler implements EventHandler {

    private String dbName;

    private String tName;

    private String eventType;

    @Override
    public boolean support(EventHeader eventHeader, String dataBaseName, String tableName) {
        if (!status) {
            return false;
        }
        EventType eventType = eventHeader.getEventType();
        if (!EventType.isRowMutation(eventType)) {
            return false;
        }
        if (eventHeader instanceof EventHeaderV4) {
            EventHeaderV4 header = (EventHeaderV4) eventHeader;
            log.info("NowPosition: {} NextPosition: ", header.getPosition(), header.getNextPosition());
        }
        this.eventType = eventType.name();
        dbName = dataBaseName;
        tName = tableName;
        return true;
    }

    @Override
    public void handle(EventBus eventBus, EventData data) {

        String[] befor = EventDataUtils.getBeforUpdate(data);
        String[] after = EventDataUtils.getAfterUpdate(data);
        String[] delete = EventDataUtils.getDeleteRows(data);
        String[] insert = EventDataUtils.getInsertRows(data);

        log.info("new {} event {}.{}: ", eventType.toLowerCase(), dbName, tName);
        if (befor != null) {
            printBefor(befor);
        }
        if (after != null) {
            printAfter(after);
        }
        if (delete != null) {
            printDelete(delete);
        }
        if (insert != null) {
            printInsert(insert);
        }

    }

    @Override
    public String getName() {
        return "print update insert delete event hander";
    }

    private void printBefor(String[] vals) {
        log.info("\tupdate befor: {}", Arrays.toString(vals));
    }

    private void printAfter(String[] vals) {
        log.info("\tupdate after: {}", Arrays.toString(vals));
    }

    private void printDelete(String[] vals) {
        log.info("\tdelete: {}", Arrays.toString(vals));
    }

    private void printInsert(String[] vals) {
        log.info("\tinsert: {}", Arrays.toString(vals));
    }
}
