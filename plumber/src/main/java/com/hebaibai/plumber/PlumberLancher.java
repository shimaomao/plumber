package com.hebaibai.plumber;

import com.hebaibai.plumber.config.Config;
import com.hebaibai.plumber.core.EventDataExecuter;
import com.hebaibai.plumber.verticle.BinLogVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
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
    private boolean run = false;

    private List<String> verticleIds = new ArrayList<>();

    /**
     * 启动
     */
    public void start(Config config) {
        //初始化Executer
        for (EventDataExecuter sqlEventDataExecuter : config.getSqlEventDataExecuters()) {
            sqlEventDataExecuter.init(vertx, config);
        }
        BinLogVerticle binLogVerticle = new BinLogVerticle(config);
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
        run = false;
    }

}
