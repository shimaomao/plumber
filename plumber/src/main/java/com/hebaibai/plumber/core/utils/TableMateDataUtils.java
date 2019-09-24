package com.hebaibai.plumber.core.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取数据库表的元数据工具类
 *
 * @author hjx
 */
@SuppressWarnings("ALL")
public class TableMateDataUtils {


    public static TableMateData getTableMateData(String createSql, String database) throws SQLException {

        TableMateData mateData = new TableMateData();

        mateData.setId(SqlUtils.getId(createSql));

        mateData.setNama(SqlUtils.getTableName(createSql));

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

}

