package com.hebaibai.plumber.config;

import io.vertx.core.json.JsonObject;
import lombok.Data;

/**
 * 数据目标配置
 *
 * @author hjx
 */
@Data
public class DataTargetConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String database;
    private String charset = "utf-8";
    private Integer maxPoolSize = 10;
    private Integer queryTimeout = 5000;
    private Integer connectTimeout = 10000;

    public JsonObject getJson() {
        return new JsonObject()
                .put("host", host)
                .put("port", port)
                .put("username", username)
                .put("password", password)
                .put("database", database)
                .put("charset", charset)
                .put("maxPoolSize", maxPoolSize)
                .put("queryTimeout", queryTimeout)
                .put("connectTimeout", connectTimeout);
    }

}
