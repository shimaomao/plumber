package com.hebaibai.plumber.verticle;


import com.hebaibai.plumber.ConsumerAddress;
import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.DataTargetConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hjx
 */
@Setter
@Slf4j
public class DataBaseVerticle extends AbstractVerticle {

    private AsyncSQLClient sqlClient;

    private DataTargetConfig targetConfig;

    public static final String DATA_BASE_POOL_NAME = "plumber_pool";

    public DataBaseVerticle(DataTargetConfig targetConfig) {
        this.targetConfig = targetConfig;
    }

    @Override
    public void start() throws Exception {
        JsonObject json = targetConfig.getJson();
        log.info("sql client :{}", json);
        this.sqlClient = MySQLClient.createShared(vertx, json, "plumber_pool:" + targetConfig.getHost());

        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_QUERY, this::query);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_INSERT, this::insert);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_DELETE, this::delete);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_UPDATE, this::update);
        log.info("start DataBaseVerticle success");
    }

    @Override
    public void stop() throws Exception {
        sqlClient.close(res -> {
            if (res.succeeded()) {
                log.info("stop AsyncSQLClient success");
            } else {
                log.info("stop AsyncSQLClient error");
            }
        });
        log.info("stop DataBaseVerticle success");
    }

    /**
     * 执行查询
     *
     * @param message
     */
    public void query(Message<String> message) {
        String sql = message.body();
        sqlClient.query(sql, res -> {
            log.debug(sql);
            if (res.succeeded()) {
                message.reply(res.result().getRows());
            } else {
                message.fail(500, res.cause().toString());
                log.error("sql-query-->失败:", res.cause());
            }
        });
    }

    /**
     * 执行更新
     *
     * @param message
     */
    public void update(Message<String> message) {
        String sql = message.body();
        sqlClient.update(sql, res -> {
            log.debug(sql);
            if (res.succeeded()) {
                message.reply(res.result().getUpdated());
            } else {
                message.fail(500, res.cause().toString());
                log.error("sql-update-->失败:", res.cause());
            }
        });
    }

    /**
     * 执行删除
     *
     * @param message
     */
    public void delete(Message<String> message) {
        String sql = message.body();
        sqlClient.update(sql, res -> {
            log.debug(sql);
            if (res.succeeded()) {
                message.reply(res.result().getUpdated());
            } else {
                message.fail(500, res.cause().toString());
                log.error("sql-delete:", res.cause());
            }
        });
    }

    /**
     * 执行新增
     *
     * @param message
     */
    public void insert(Message<String> message) {
        String sql = message.body();
        sqlClient.update(sql, res -> {
            log.debug(sql);
            if (res.succeeded()) {
                message.reply(res.result().getUpdated());
            } else {
                message.fail(500, res.cause().toString());
                log.error("sql-insert:", res.cause());
            }
        });
    }
}
