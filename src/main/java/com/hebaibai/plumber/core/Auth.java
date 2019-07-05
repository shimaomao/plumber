package com.hebaibai.plumber.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于数据库的链接
 * @author hjx
 */
@Data
public class Auth {

    private String hostname = "localhost";

    private int port = 3306;

    private String username;

    private String password;

    private String mark;

    private TableIdMapping tableIdMapping = new TableIdMapping();


    public void seveTableName(long tableId, String tableName) {
        tableIdMapping.tableNameTableIdMap.put(tableId, tableName);
    }

    public void seveDatabaseName(long tableId, String databaseName) {
        tableIdMapping.tableIdDatabaseName.put(tableId, databaseName);
    }

    public String getTableName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return tableIdMapping.tableNameTableIdMap.get(tableId);
    }

    public String getDatabaseName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return tableIdMapping.tableIdDatabaseName.get(tableId);
    }

    /**
     * 数据库元数据
     */
    private class TableIdMapping {

        /**
         * key sourceDatabase.sourceTable
         * value tableId
         */
        Map<Long, String> tableNameTableIdMap = new HashMap<>();

        /**
         * key sourceDatabase.sourceTable
         */
        Map<Long, String> tableIdDatabaseName = new HashMap<>();


    }

}