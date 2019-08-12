package com.hebaibai.plumber;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.handler.InsertUpdateDeleteEventHandlerImpl;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.hebaibai.plumber.core.utils.TableMateDataUtils;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * 程序入口
 *
 * @author hjx
 */
@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("Main start ... ");
        Config config = new Config();
        try (FileInputStream inputStream = new FileInputStream(getConf(args))) {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            JSONObject jsonObject = JSONObject.parseObject(new String(bytes, "utf-8"));
            //数据源配置
            DataSourceConfig dataSource = jsonObject.getObject("dataSource", DataSourceConfig.class);
            config.setDataSourceConfig(dataSource);
            //目标数据配置
            DataTargetConfig dataTarget = jsonObject.getObject("dataTarget", DataTargetConfig.class);
            config.setDataTargetConfig(dataTarget);

            Connection dataSourceConn = getConnection(dataSource.getHost(), dataSource.getPort(), dataSource.getUsername(), dataSource.getPassword());
            Connection dataTargetConn = getConnection(dataTarget.getHost(), dataTarget.getPort(), dataTarget.getUsername(), dataTarget.getPassword());

            //eventHandler
            JSONArray eventHandlerArray = jsonObject.getJSONArray("eventHandler");
            Set<EventHandler> eventHandlers = new HashSet();
            config.setEventHandlers(eventHandlers);
            for (int i = 0; i < eventHandlerArray.size(); i++) {
                EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
                eventHandler.setStatus(true);

                JSONObject eventHandlerJson = eventHandlerArray.getJSONObject(i);
                //mapping
                JSONObject mapping = eventHandlerJson.getJSONObject("mapping");
                Set<String> keySet = mapping.keySet();
                Map<String, String> map = new HashMap<>();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    map.put(key, mapping.getString(key));
                }
                eventHandler.setMapping(map);
                //keys
                JSONArray keysJson = eventHandlerJson.getJSONArray("keys");
                Set<String> keys = new HashSet<>();
                for (int j = 0; j < keysJson.size(); j++) {
                    String key = keysJson.getString(j);
                    keys.add(key);
                }
                eventHandler.setKeys(keys);
                //source table
                JSONObject source = eventHandlerJson.getJSONObject("source");
                String sourceDb = source.getString("dbName");
                String sourceTable = source.getString("tableName");
                String sourceSql = getCreateSql(dataSourceConn, sourceDb, sourceTable);
                TableMateData sourceMateData = TableMateDataUtils.getTableMateData(sourceSql, sourceDb);
                eventHandler.setSource(sourceMateData);
                //source table
                JSONObject target = eventHandlerJson.getJSONObject("target");
                String targetDb = target.getString("dbName");
                String targetTable = target.getString("tableName");
                String targetSql = getCreateSql(dataTargetConn, targetDb, targetTable);
                TableMateData targetMateData = TableMateDataUtils.getTableMateData(targetSql, targetDb);
                eventHandler.setSource(targetMateData);
                //add
                eventHandlers.add(eventHandler);
            }
            dataSourceConn.close();
            dataTargetConn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
            return;
        }

        PlumberLancher plumberLancher = new PlumberLancher();
        Vertx vertx = Vertx.vertx();
        Context context = vertx.getOrCreateContext();
        plumberLancher.setVertx(vertx);
        plumberLancher.setContext(context);
        plumberLancher.start(config);
        log.info("Main start success ...");
    }

    /**
     * 获取配置文件
     *
     * @param args
     * @return
     * @throws ParseException
     */
    private static File getConf(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("c", "conf", true, "config file path");
        CommandLine parse = new BasicParser().parse(options, args);
        if (!parse.hasOption("conf")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
            System.exit(0);
        }
        String conf = parse.getOptionValue("conf");
        if (conf.startsWith(File.separator)) {
            return new File(conf);
        }
        String path = System.getProperty("user.dir");
        return new File(path + "/" + conf);
    }

    private static Connection getConnection(String host, int port, String user, String pwd) throws SQLException {
        Connection connection = (Connection) DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "?characterEncoding=utf-8", user, pwd
        );
        return connection;
    }

    private static String getCreateSql(Connection connection, String database, String table) throws SQLException {
        String sql = "show create table " + database + "." + table;
        List<Map<String, Object>> maps = new QueryRunner().query(connection, sql, new MapListHandler());
        Map<String, Object> map = maps.get(0);
        return map.get("Create Table").toString();
    }
}
