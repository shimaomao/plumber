package com.hebaibai.plumber;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用于数据库的链接
 *
 * @author hjx
 */
public class DataSourceConfig {

    @Getter
    @Setter
    private String hostname = "localhost";

    @Getter
    @Setter
    private int port = 3306;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataSourceConfig dataSourceConfig = (DataSourceConfig) o;
        return port == dataSourceConfig.port &&
                Objects.equals(hostname, dataSourceConfig.hostname) &&
                Objects.equals(username, dataSourceConfig.username) &&
                Objects.equals(password, dataSourceConfig.password) &&
                Objects.equals(mark, dataSourceConfig.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostname, port, username, password, mark);
    }
}