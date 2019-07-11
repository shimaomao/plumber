package com.hebaibai.plumber.verticle;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.core.BinlogEventListener;
import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.handler.TableMapEventHandlerImpl;
import io.vertx.core.AbstractVerticle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取binlog
 */
@Slf4j
public class BinLogVerticle extends AbstractVerticle {

    /**
     * 兼容模式
     */
    private final static EventDeserializer eventDeserializer = new EventDeserializer();

    static {
        eventDeserializer.setCompatibilityMode(EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY);
    }

    @Getter
    private DataSourceConfig dataSourceConfig;

    private BinlogThread binlogThread = new BinlogThread();

    private BinlogEventListener binlogEventListener;

    @Setter
    private Set<EventHandler> eventHandlers;

    public BinLogVerticle(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    @Override
    public void start() {
        binlogThread.binaryLogClient = new BinaryLogClient(
                dataSourceConfig.getHostname(),
                dataSourceConfig.getPort(),
                dataSourceConfig.getUsername(),
                dataSourceConfig.getPassword()
        );
        this.binlogEventListener = new BinlogEventListener(dataSourceConfig, vertx.eventBus());
        //监控表，数据库
        if (this.eventHandlers == null) {
            eventHandlers = new HashSet<>();
        }
        EventHandler eventHandler = new TableMapEventHandlerImpl();
        eventHandler.setDataSourceConfig(dataSourceConfig);
        this.eventHandlers.add(eventHandler);
        //其他的处理器
        this.binlogEventListener.setEventHandlers(this.eventHandlers);
        binlogThread.binaryLogClient.setEventDeserializer(eventDeserializer);
        binlogThread.binaryLogClient.registerEventListener(this.binlogEventListener);
        //设置binlog_client线程名称
        binlogThread.setName("binlog_client:" + dataSourceConfig.getHostname());
        binlogThread.start();
        log.info("start BinLogVerticle success");
    }

    @Override
    public void stop() throws Exception {
        binlogThread.run = false;
        try {
            binlogThread.binaryLogClient.disconnect();
        } catch (IOException e) {
            log.info("disconnect binaryLogClient error", e);
        }
        log.info("disconnect binaryLogClient success");
        log.info("stop BinLogVerticle success");
    }

    private class BinlogThread extends Thread {

        private Logger log = LoggerFactory.getLogger(getName());

        private BinaryLogClient binaryLogClient;

        transient private boolean run = false;

        @Override
        public void run() {
            while (run) {
                try {
                    binaryLogClient.connect();
                } catch (IOException e) {
                    log.info("binaryLogClient connect error", e);
                }
            }
            log.info("stop Thread: {} success", Thread.currentThread().getName());
        }

        @Override
        public synchronized void start() {
            this.run = true;
            super.start();
        }
    }
}
