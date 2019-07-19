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

    @Setter
    @Getter
    private Context context;

    @Getter
    @Setter
    private Config config;

    private AsyncSQLClient sqlClient;

    @Getter
    private boolean run = false;

    private List<String> verticleIds = new ArrayList<>();

    public PlumberLancher(Config config) {
        this.config = config;
    }

    /**
     * 启动
     */
    public void start() {
        DataTargetConfig dataTargetConfig = config.getDataTargetConfig();
        JsonObject json = dataTargetConfig.getJson();
        log.info("sql client :{}", json);
        sqlClient = MySQLClient.createShared(vertx, json, "plumber_pool:" + dataTargetConfig.getHost());

        DataBaseVerticle dataBaseVerticle = new DataBaseVerticle(sqlClient);
        dataBaseVerticle.init(vertx, context);
        vertx.deployVerticle(dataBaseVerticle, res -> {
            if (res.succeeded()) {
                String id = res.result();
                verticleIds.add(id);
                log.info("DataBaseVerticle {} 部署成功", id);
            }
        });

        BinLogVerticle binLogVerticle = new BinLogVerticle(this.config);
        binLogVerticle.init(vertx, context);
        vertx.deployVerticle(binLogVerticle, res -> {
            if (res.succeeded()) {
                String id = res.result();
                verticleIds.add(id);
                log.info("BinLogVerticle {} 部署成功", id);
            }
        });
        run = true;
    }

    public void stop() {
        for (String verticleId : verticleIds) {
            this.vertx.undeploy(verticleId, res -> {
                if (res.succeeded()) {
                    log.info("verticle {} 停止成功", verticleId);
                }
            });
        }
        sqlClient.close(res -> {
            if (res.succeeded()) {
                log.info("stop AsyncSQLClient success");
            } else {
                log.info("stop AsyncSQLClient error");
            }
        });
        run = false;
    }

    /**
     * 获取当前的verticleIds
     *
     * @return
     */
    private List<String> verticleIds() {
        return verticleIds;
    }
}
