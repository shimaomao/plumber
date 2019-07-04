package com.hebaibai.plumber.core;

import lombok.Data;

import java.util.Map;

/**
 * 数据库来目标表的映
 */
@Data
public class TargetTable {

    /**
     * 目标数据库表
     */
    private String table;

    /**
     * 目标数据库
     */
    private String database;

    /**
     * key:     来源字段名称
     * value:   目标字段名称
     */
    private Map<String, String> columnMap;

    /**
     * key:     来源Key字段名称
     * value:   目标Key字段名称
     */
    private Map<String, String> keyColumnMap;

    /**
     * key:     来源字段类型
     * value:   目标字段类型
     */
    private Map<String, String> columnTypeMap;


}
