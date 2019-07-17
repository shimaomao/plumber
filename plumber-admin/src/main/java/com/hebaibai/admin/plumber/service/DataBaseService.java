package com.hebaibai.admin.plumber.service;

import com.hebaibai.admin.plumber.TableMateDataUtils;
import com.hebaibai.admin.plumber.entity.DataConfig;
import com.hebaibai.admin.plumber.entity.Plumber;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.mysql.jdbc.Connection;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class DataBaseService {

    @Autowired
    private IPlumberService iPlumberService;

    /**
     * 获取实例中的数据库名称
     *
     * @param type      1:source; 0:target
     * @param plumberId
     * @return
     */
    public Map<String, List<String>> findAllDataBase(String plumberId) throws SQLException {
        Map<String, List<String>> names = new HashMap<>();
        Plumber plumber = iPlumberService.getById(plumberId);
        if (plumber == null) {
            return null;
        }
        String sql = "select SCHEMA_NAME from information_schema.SCHEMATA";
        //来源库
        List<String> source = new ArrayList<>();
        List<Map<String, Object>> sourceMaps = query(plumber.getSource(), sql);
        for (Map<String, Object> database : sourceMaps) {
            source.add(database.get("SCHEMA_NAME") + "");
        }
        //目标库
        List<String> target = new ArrayList<>();
        List<Map<String, Object>> targetMaps = query(plumber.getSource(), sql);
        for (Map<String, Object> database : targetMaps) {
            target.add(database.get("SCHEMA_NAME") + "");
        }
        names.put("1", source);
        names.put("0", target);
        return names;
    }

    public List<String> findAllTable(int type, String dataBase, String plumberId) throws SQLException {
        ArrayList names = new ArrayList();
        Plumber plumber = iPlumberService.getById(plumberId);
        if (plumber == null) {
            return null;
        }
        DataConfig dataConfig = null;
        if (type == 1) {
            dataConfig = plumber.getSource();
        } else {
            dataConfig = plumber.getTarget();
        }
        List<Map<String, Object>> maps = query(dataConfig,
                "select TABLE_NAME\n" +
                        "from\n" +
                        "  information_schema.TABLES\n" +
                        "where TABLE_SCHEMA = '" + dataBase + "';\n");
        for (Map<String, Object> database : maps) {
            names.add(database.get("TABLE_NAME"));
        }
        return names;
    }

    public TableMateData getTableMateData(int type, String dataBase, String plumberId, String table) throws SQLException {
        Plumber plumber = iPlumberService.getById(plumberId);
        if (plumber == null) {
            return null;
        }
        DataConfig dataConfig = null;
        if (type == 1) {
            dataConfig = plumber.getSource();
        } else {
            dataConfig = plumber.getTarget();
        }
        TableMateDataUtils dataUtils = new TableMateDataUtils(dataConfig, dataBase, table);
        TableMateData tableMateData = dataUtils.getTableMateData();
        return tableMateData;
    }

    List<Map<String, Object>> query(DataConfig dataConfig, String sql) throws SQLException {
        Connection conn = (Connection) DriverManager.getConnection(
                "jdbc:mysql://" + dataConfig.getHost() + ":" + dataConfig.getPort() + "?characterEncoding=utf-8",
                dataConfig.getUser(),
                dataConfig.getPwd()
        );
        List<Map<String, Object>> maps = new QueryRunner().query(conn, sql, new MapListHandler());
        DbUtils.close(conn);
        return maps;
    }
}
