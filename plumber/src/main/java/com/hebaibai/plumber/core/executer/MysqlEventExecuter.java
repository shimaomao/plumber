package com.hebaibai.plumber.core.executer;

import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.core.SqlEventData;
import com.hebaibai.plumber.core.SqlEventDataExecuter;
import io.vertx.core.eventbus.EventBus;

/**
 * 默认的实现插件
 */
public class MysqlEventExecuter implements SqlEventDataExecuter {
    @Override
    public void setConfig(JSONObject config) {

    }

    @Override
    public void execute(EventBus eventBus, SqlEventData eventPluginData) throws Exception {
        String type = eventPluginData.getType();
        switch (type) {
            case SqlEventData.TYPE_INSERT:
                break;
            case SqlEventData.TYPE_DELETE:
                break;
            case SqlEventData.TYPE_UPDATE:
                break;
            default:
                break;
        }
    }

}
