package com.hebaibai.plumber.core;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import com.hebaibai.plumber.core.handler.EventHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * 一个实例
 */
@Slf4j
class AuthBinaryLogApp implements Runnable {

    /**
     * 兼容模式
     */
    public static EventDeserializer eventDeserializer;

    static {
        eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                //char 和 binary 类型数据转换为 byte[]
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
    }

    /**
     * 是否启动
     */
    private boolean runing = false;

    @Getter
    private Auth auth;

    private ExecutorService executorService;

    private BinaryLogClient binaryLogClient;

    private AuthEventListener authEventListener;

    public AuthBinaryLogApp(Auth auth, @NonNull ExecutorService executorService) {
        this.auth = auth;
        this.executorService = executorService;

        this.authEventListener = new AuthEventListener(auth, executorService);
        this.binaryLogClient = new BinaryLogClient(
                auth.getHostname(),
                auth.getPort(),
                auth.getUsername(),
                auth.getPassword()
        );
        this.binaryLogClient.setEventDeserializer(eventDeserializer);
        this.binaryLogClient.registerEventListener(this.authEventListener);
    }


    @Override
    public void run() {
        while (runing) {
            try {
                log.info("binaryLogClient.connect start ...");
                binaryLogClient.connect();
            } catch (IOException e) {
                log.error("binaryLogClient.connect start error ...", e);
            }
        }

    }

    boolean start() {
        runing = true;
        return binaryLogClient.isConnected();
    }

    public boolean stop() {
        runing = false;
        try {
            log.info("binaryLogClient.connect stop ...");
            binaryLogClient.disconnect();
        } catch (IOException e) {
            log.info("binaryLogClient.connect stop error ...", e);
        }
        return !binaryLogClient.isConnected();
    }


    public void registEventHandler(EventHandler handle) {
        this.authEventListener.getEventHandlers().add(handle);
    }
}
