package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.hebaibai.plumber.core.utils.TableMateData;
import io.vertx.core.eventbus.EventBus;

import java.util.Map;
import java.util.Set;

/**
 * binlog的事件处理器
 *
 * @author hjx
 */
public interface EventHandler {


    /**
     * 设置目标数据源
     *
     * @param tableMateData
     */
    void setTarget(TableMateData tableMateData);

    /**
     * 设置来源数据
     *
     * @param tableMateData
     */
    void setSource(TableMateData tableMateData);


    /**
     * 是否支持当前操作
     *
     * @param eventHeader
     * @param dataBaseName
     * @param tableName
     * @return
     */
    boolean support(EventHeader eventHeader, String dataBaseName, String tableName);


    /**
     * 设置状态
     *
     * @param isRun true:可用
     * @param isRun false:不可用
     * @return
     */
    void setStatus(boolean isRun);

    /**
     * 处理
     *
     * @param eventBus
     * @param data
     * @return
     */
    void handle(EventBus eventBus, EventData data);

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 字段映射
     * key      source 字段
     * value    target 字段
     *
     * @param mapping
     */
    void setMapping(Map<String, String> mapping);

    /**
     * 来源表中的key
     *
     * @param keys
     */
    void setKeys(Set<String> keys);
}
