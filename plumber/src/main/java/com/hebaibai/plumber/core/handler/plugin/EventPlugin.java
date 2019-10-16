package com.hebaibai.plumber.core.handler.plugin;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件插件, 用于处理其他以一些东西
 *
 * @author hjx
 */
public interface EventPlugin {

    String TYPE_INSERT = "TYPE_INSERT";
    String TYPE_UPDATE = "TYPE_UPDATE";
    String TYPE_DELETE = "TYPE_DELETE";

    /**
     * 现有的插件
     */
    Map<String, Class<? extends EventPlugin>> EVENT_PLUGIN_MAP = new HashMap() {{
        put(DefaultEventPlugin.class.getSimpleName(), DefaultEventPlugin.class);
    }};

    /**
     * 设置其他配置
     *
     * @param config
     */
    void setConfig(JSONObject config);


    /**
     * 开始处理数据
     *
     * @return
     */
    void doWithPlugin(String type, EventPluginData eventPluginData) throws Exception;

}
