package com.hebaibai.plumber.core.handler.plugin;

import com.alibaba.fastjson.JSONObject;

/**
 * 默认的实现插件
 */
public class DefaultEventPlugin implements EventPlugin {
    @Override
    public void setConfig(JSONObject config) {

    }

    @Override
    public void doWithPlugin(String type, EventPluginData eventPluginData) throws Exception {
        System.out.println("");
    }
}
