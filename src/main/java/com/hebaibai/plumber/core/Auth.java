package com.hebaibai.plumber.core;

import com.github.shyiko.mysql.binlog.event.TableMapEventMetadata;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于数据库的链接
 */
@Data
public class Auth {

    private String hostname = "localhost";

    private int port = 3306;

    private String username;

    private String password;

    private String mark;

    private DataBaseMateData dataBaseMateData = new DataBaseMateData();


    public void seveTableName(long tableId, String tableName) {
        dataBaseMateData.tableNameTableIdMap.put(tableId, tableName);
    }

    public void seveDatabaseName(long tableId, String databaseName) {
        dataBaseMateData.tableIdDatabaseName.put(tableId, databaseName);
    }

    public String getTableName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return dataBaseMateData.tableNameTableIdMap.get(tableId);
    }

    public String getDatabaseName(Long tableId) {
        if (tableId == null) {
            return null;
        }
        return dataBaseMateData.tableIdDatabaseName.get(tableId);
    }

    /**
     * 数据库元数据
     */
    private class DataBaseMateData {

        /**
         * key database.table
         * value tableId
         */
        Map<Long, String> tableNameTableIdMap = new HashMap<>();

        /**
         * key database.table
         */
        Map<Long, String> tableIdDatabaseName = new HashMap<>();

        Map<Long, TableMapEventMetadata> tableIdMetadata = new HashMap<>();

    }

}