package com.hebaibai.admin.plumber;

import com.hebaibai.admin.plumber.entity.DataConfig;
import com.hebaibai.plumber.core.utils.SqlUtils;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.mysql.jdbc.Connection;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    private DataConfig dataConfig;

    private String database;

    private String table;

    public TableMateDataUtils(DataConfig dataConfig, String database, String table) {
        this.database = database;
        this.table = table;
        this.dataConfig = dataConfig;
    }

    public TableMateData getTableMateData() throws SQLException {
        String createSql = getCreateSql(dataConfig);
        TableMateData mateData = new TableMateData();
        mateData.setNama(table);
        mateData.setDataBase(database);
        //字段信息
        List<String> columnSqls = SqlUtils.getColumnSqls(createSql);

        List<String> columns = new ArrayList<>(columnSqls.size());
        mateData.setColumns(columns);

        Map<String, String> columnTypeMap = new HashMap<>(columnSqls.size());
        mateData.setColumnType(columnTypeMap);
        Map<String, String> columnDoceMap = new HashMap<>(columnSqls.size());
        mateData.setColumnDoc(columnDoceMap);

        for (String columnSql : columnSqls) {
            String columnName = SqlUtils.getByPattern(columnSql, "`(.*)`", 1);
            columns.add(columnName);

            String columnType = SqlUtils.getByPattern(columnSql, "`" + columnName + "` ([A-Za-z]*)", 1);
            columnTypeMap.put(columnName, columnType);

            String columnComment = SqlUtils.getColumnComment(columnSql);
            columnDoceMap.put(columnName, columnComment);
        }
        return mateData;
    }

    private String getCreateSql(DataConfig dataConfig) throws SQLException {
        Connection connection = (Connection) DriverManager.getConnection(
                "jdbc:mysql://" + dataConfig.getHost() + ":" + dataConfig.getPort() + "?characterEncoding=utf-8",
                dataConfig.getUser(),
                dataConfig.getPwd()
        );
        List<Map<String, Object>> maps = new QueryRunner().query(connection, "show create table " + database + "." + table, new MapListHandler());
        Map<String, Object> map = maps.get(0);
        System.out.println(1);
        return map.get("Create Table").toString();
    }
}

