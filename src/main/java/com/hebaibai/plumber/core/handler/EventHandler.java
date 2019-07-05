package com.hebaibai.plumber.core.handler;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.hebaibai.plumber.core.Auth;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * @author hjx
 */
public interface EventHandler {


    /**
     * 设置目标数据源
     *
     * @param dataSource
     * @param database
     * @param table
     * @throws SQLException
     */
    void setTarget(DataSource dataSource, String database, String table) throws SQLException;

    /**
     * 设置来源数据
     *
     * @param auth
     * @param database
     * @param table
     * @throws SQLException
     */
    void setSource(Auth auth, String database, String table) throws SQLException;


    /**
     * 是否支持当前操作
     *
     * @param eventType
     * @param dataBaseName
     * @param tableName
     * @return
     */
    boolean support(EventType eventType, String dataBaseName, String tableName);


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
     * @param data
     * @return
     */
    Runnable handle(EventData data);

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
