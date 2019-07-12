package com.hebaibai.plumber;

import com.hebaibai.plumber.verticle.BinLogVerticle;
import com.hebaibai.plumber.verticle.DataBaseVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private Config config;

    private String uuid;

    private List<String> verticleIds = new ArrayList<>();

    public PlumberLancher(Config config) {
        this.config = config;
    }

    /**
     * 启动
     */
    public String start() {
        String sourceHost = config.getDataSourceConfig().getHostname();
        String targetHost = config.getDataTargetConfig().getHost();
        DataBaseVerticle dataBaseVerticle = new DataBaseVerticle(config.getDataTargetConfig());
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
        //占位
        uuid = UUID.randomUUID().toString();
        context.put(sourceHost + ":" + targetHost, uuid);
        return uuid;
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

    /**
     * 获取当前的verticleIds
     *
     * @return
     */
    public List<String> verticleIds() {
        return verticleIds;
    }
}
