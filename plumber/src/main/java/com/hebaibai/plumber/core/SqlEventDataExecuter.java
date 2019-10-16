package com.hebaibai.plumber.core;

import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.config.Config;
import com.hebaibai.plumber.core.executer.MysqlEventExecuter;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件插件, 用于处理其他以一些东西
 *
 * @author hjx
 */
public interface SqlEventDataExecuter {

    /**
     * 现有的插件
     */
    Map<String, Class<? extends SqlEventDataExecuter>> EVENT_PLUGIN_MAP = new HashMap() {{
        put(MysqlEventExecuter.class.getSimpleName(), MysqlEventExecuter.class);
    }};

    /**
     * 设置其他配置
     *
     * @param config
     */
    void setConfig(JSONObject config);

    /**
     * 初始化
     *
     * @param vertx
     */
    void init(Vertx vertx, Config config);


    /**
     * 开始处理数据
     *
     * @return
     */
    void execute(SqlEventData eventPluginData) throws Exception;

}
