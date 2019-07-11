package com.hebaibai.plumber;

import io.vertx.core.json.JsonObject;
import lombok.Data;

@Data
public class DataTargetConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String charset;
    private int maxPoolSize;
    private long queryTimeout;
    private long connectTimeout;

    public JsonObject getJson() {
        return new JsonObject()
                .put("host", host)
                .put("port", port)
                .put("username", username)
                .put("password", password)
                .put("charset", charset)
                .put("maxPoolSize", maxPoolSize)
                .put("queryTimeout", queryTimeout)
                .put("connectTimeout", connectTimeout);
    }

}
