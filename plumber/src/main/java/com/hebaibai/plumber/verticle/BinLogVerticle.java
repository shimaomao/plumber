package com.hebaibai.plumber.verticle;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.hebaibai.plumber.config.Config;
import com.hebaibai.plumber.core.BinlogEventListener;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 获取binlog的Verticle
 *
 * @author hjx
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

    private BinlogThread binlogThread = new BinlogThread();

    private BinlogEventListener binlogEventListener;

    private Config config;

    public BinLogVerticle(Config config) {
        this.config = config;
    }

    @Override
    public void start() {
        //BinaryLogClient
        binlogThread.binaryLogClient = new BinaryLogClient(
                config.getDataSourceConfig().getHost(),
                config.getDataSourceConfig().getPort(),
                config.getDataSourceConfig().getUsername(),
                config.getDataSourceConfig().getPassword()
        );
        //设置事件处理器
        this.binlogEventListener = new BinlogEventListener(vertx.eventBus());
        binlogThread.binaryLogClient.registerEventListener(this.binlogEventListener);
        //设置模式
        binlogThread.binaryLogClient.setEventDeserializer(eventDeserializer);
        //binlog名称
        if (config.getLogName() != null) {
            binlogThread.binaryLogClient.setBinlogFilename(config.getLogName());
            //binlog位置
            if (config.getPosition() != null) {
                binlogThread.binaryLogClient.setBinlogPosition(config.getPosition());
            }
        }
        log.info("start BinaryLogClient BinlogFilename:{}, BinlogPosition:{}", config.getLogName(), config.getPosition());
        //用户定义的事件处理器
        this.binlogEventListener.setEventHandlers(config.getEventHandlers());
        //设置binlog_client线程名称
        binlogThread.setName("BinLogVerticle:" + config.getDataSourceConfig().getHost());
        binlogThread.start();
        log.info("start BinLogVerticle success");
    }

    @Override
    public void stop() throws Exception {
        binlogThread.run = false;
        try {
            binlogThread.binaryLogClient.disconnect();
        } catch (IOException e) {
            log.info("disconnect BinaryLogClient error", e);
        }
        log.info("disconnect BinaryLogClient success");
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
