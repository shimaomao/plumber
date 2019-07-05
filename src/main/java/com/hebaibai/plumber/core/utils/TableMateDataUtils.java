package com.hebaibai.plumber.core.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取数据库表的元数据工具类
 *
 * @author hjx
 */
@Slf4j
@Setter
@Getter
public class TableMateDataUtils {

    private Connection connection;

    private String database;

    private String table;

    public TableMateDataUtils(String hostname, int port, String username, String password, String database, String table) throws SQLException {
        this.database = database;
        this.table = table;
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "?characterEncoding=utf-8";
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        this.connection = connection;
    }

    public TableMateDataUtils(DataSource dataSource, String database, String table) throws SQLException {
        this.database = database;
        this.table = table;
        Connection connection = dataSource.getConnection();
        this.connection = connection;
    }

    public TableMateData getTableMateData() throws SQLException {
        String createSql = getCreateSql(connection);
        TableMateData mateData = new TableMateData();
        mateData.setNama(table);
        //字段信息
        List<String> columnSqls = SqlUtils.getColumnSqls(createSql);

        List<String> columns = new ArrayList<>(columnSqls.size());
        mateData.setColumns(columns);

        Map<String, String> columnTypeMap = new HashMap<>(columnSqls.size());
        mateData.setColumnType(columnTypeMap);

        for (String columnSql : columnSqls) {
            String columnName = SqlUtils.getByPattern(columnSql, "`(.*)`", 1);
            columns.add(columnName);

            String columnType = SqlUtils.getByPattern(columnSql, "`" + columnName + "` ([A-Za-z]*)", 1);
            columnTypeMap.put(columnName, columnType);
        }
        return mateData;
    }

    private String getCreateSql(Connection connection) throws SQLException {

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show create table " + database + "." + table)) {
            while (resultSet.next()) {
                return resultSet.getString(2);
            }
        } finally {
            connection.close();
        }
        return null;
    }
}

