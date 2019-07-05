package com.hebaibai.plumber.core;

import com.hebaibai.plumber.core.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author hjx
 */
@Slf4j
@Component
public class BinaryLogClientService {

    /**
     * 保存的认证信息
     */
    private Map<Auth, AuthBinaryLogApp> binaryLogAppMap = new HashMap<>();

    @Autowired
    private ExecutorService executorService;

    /**
     * 获取 BinaryLogClient
     *
     * @param auth
     * @return
     */
    public void create(Auth auth) {
        if (binaryLogAppMap.containsKey(auth)) {
            throw new UnsupportedOperationException(auth.getHostname() + "已经存在");
        }
        AuthBinaryLogApp authBinaryLogApp = new AuthBinaryLogApp(auth, executorService);
        binaryLogAppMap.put(auth, authBinaryLogApp);
    }

    /**
     * 启动一个BinaryLogApp
     *
     * @param auth
     * @return
     */
    public void start(Auth auth) {
        hasAuth(auth);
        AuthBinaryLogApp authBinaryLogApp = binaryLogAppMap.get(auth);
        authBinaryLogApp.start();
        executorService.execute(authBinaryLogApp);
    }

    /**
     * 停止一个BinaryLogApp
     *
     * @param auth
     * @return
     */
    public void stop(Auth auth) {
        hasAuth(auth);
        AuthBinaryLogApp authBinaryLogApp = binaryLogAppMap.get(auth);
        authBinaryLogApp.stop();
    }


    /**
     * 注册一个事件处理器
     *
     * @param auth
     * @param handle
     */
    public void registEventHandler(Auth auth, EventHandler handle) {
        hasAuth(auth);
        AuthBinaryLogApp authBinaryLogApp = binaryLogAppMap.get(auth);
        authBinaryLogApp.registEventHandler(handle);
    }

    private void hasAuth(Auth auth) {
        if (binaryLogAppMap.containsKey(auth)) {
            return;
        }
        throw new UnsupportedOperationException(auth.getHostname() + "不存在");
    }

}
