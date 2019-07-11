package com.hebaibai.plumber;

import com.hebaibai.plumber.verticle.BinLogVerticle;
import com.hebaibai.plumber.verticle.DataBaseVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动器
 *
 * @author hjx
 */
@Slf4j
public class PlumberLancher {

    @Getter
    @Setter
    private Vertx vertx;

    @Getter
    @Setter
    private Context context;

    private Config config;

    private List<String> verticleIds = new ArrayList<>();

    public PlumberLancher(Config config) {
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
        dataBaseVerticle.init(vertx, context);
        vertx.deployVerticle(dataBaseVerticle, res -> {
            if (res.succeeded()) {
                String id = res.result();
                verticleIds.add(id);
                log.info("DataBaseVerticle {} 部署成功", id);
            }
        });

        BinLogVerticle binLogVerticle = new BinLogVerticle(this.config.getDataSourceConfig());
        binLogVerticle.setEventHandlers(this.config.getEventHandlers());
        binLogVerticle.init(vertx, context);
        vertx.deployVerticle(binLogVerticle, res -> {
            if (res.succeeded()) {
                String id = res.result();
                verticleIds.add(id);
                log.info("BinLogVerticle {} 部署成功", id);
            }
        });
    }

    public void stop() {
        for (String verticleId : verticleIds) {
            this.vertx.undeploy(verticleId, res -> {
                if (res.succeeded()) {
                    log.info("verticle {} 停止成功", verticleId);
                }
            });
        }
    }
}
