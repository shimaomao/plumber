package com.hebaibai.plumber.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库元数据
 * @author hjx
 */
public class TableIdMapping {

    /**
     * key sourceDatabase.sourceTable
     * value tableId
     */
    private Map<Long, String> tableNameTableIdMap = new HashMap<>();

    /**
     * key sourceDatabase.sourceTable
     */
    private Map<Long, String> tableIdDatabaseName = new HashMap<>();

    public void seveTableName(long tableId, String tableName) {
        tableNameTableIdMap.put(tableId, tableName);
    }

    public void seveDatabaseName(long tableId, String databaseName) {
        tableIdDatabaseName.put(tableId, databaseName);
    }

    public String getTableName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return tableNameTableIdMap.get(tableId);
    }

    public String getDatabaseName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return tableIdDatabaseName.get(tableId);
    }
}