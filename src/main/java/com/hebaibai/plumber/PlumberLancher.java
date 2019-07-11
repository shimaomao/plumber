package com.hebaibai.plumber;

import com.hebaibai.plumber.verticle.BinLogVerticle;
import com.hebaibai.plumber.verticle.DataBaseVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动器
 * @author hjx
 */
public class PlumberLancher {

    private Vertx vertx;

    private Config config;

    private List<String> verticleIds = new ArrayList<>();

    public PlumberLancher(Config config) {
        this.vertx = Vertx.vertx();
        this.config = config;
    }

    /**
     * 启动
     */
    public void start() {
        JsonObject json = this.config.getDataTargetConfig().getJson();
        DataSourceConfig dataSourceConfig = this.config.getDataSourceConfig();
        AsyncSQLClient client = MySQLClient.createShared(vertx, json, "plumber_pool:" + dataSourceConfig.getHostname());
        DataBaseVerticle dataBaseVerticle = new DataBaseVerticle(client);
        vertx.deployVerticle(dataBaseVerticle);

        BinLogVerticle binLogVerticle = new BinLogVerticle(this.config.getDataSourceConfig());
        binLogVerticle.setEventHandlers(this.config.getEventHandlers());
        vertx.deployVerticle(binLogVerticle);
    }

    public void stop() {
        for (String verticleId : verticleIds) {
            this.vertx.undeploy(verticleId);
        }
    }
}
