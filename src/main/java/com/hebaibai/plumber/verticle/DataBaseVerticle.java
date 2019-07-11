package com.hebaibai.plumber.verticle;


import com.hebaibai.plumber.ConsumerAddress;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hjx
 */
@Setter
@Slf4j
public class DataBaseVerticle extends AbstractVerticle {

    private AsyncSQLClient sqlClient;

    public static final String DATA_BASE_POOL_NAME = "plumber_pool";

    public DataBaseVerticle(AsyncSQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_QUERY, this::query);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_INSERT, this::insert);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_DELETE, this::delete);
        eventBus.consumer(ConsumerAddress.EXECUTE_SQL_UPDATE, this::update);
        log.info("init DataBaseVerticle success");
    }


    /**
     * 执行查询
     *
     * @param message
     */
    public void query(Message<String> message) {
        sqlClient.query(message.body(), res -> {
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
        sqlClient.update(message.body(), res -> {
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
        sqlClient.update(message.body(), res -> {
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
        sqlClient.update(message.body(), res -> {
            if (res.succeeded()) {
                message.reply(res.result().getUpdated());
            } else {
                message.fail(500, res.cause().toString());
                log.error("sql-insert:", res.cause());
            }
        });
    }
}
