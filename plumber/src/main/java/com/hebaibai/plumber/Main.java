package com.hebaibai.plumber;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.config.Config;
import com.hebaibai.plumber.config.DataSourceConfig;
import com.hebaibai.plumber.config.DataTargetConfig;
import com.hebaibai.plumber.core.EventHandler;
import com.hebaibai.plumber.core.handler.InsertUpdateDeleteEventHandlerImpl;
import com.hebaibai.plumber.core.executer.MysqlEventExecuter;
import com.hebaibai.plumber.core.SqlEventDataExecuter;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.hebaibai.plumber.core.utils.TableMateDataUtils;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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


    /**
     * binlog 名称
     */
    public static final String LOG_NAME = "log-name";

    /**
     * binlog 位置
     */
    public static final String LOG_POSITION = "log-position";

    /**
     * 数据来源
     */
    public static final String DATA_SOURCE = "data-source";

    /**
     * 数据目标
     */
    public static final String DATA_TARGET = "data-target";

    /**
     * 数据同步
     */
    public static final String TABLE_SYNC_JOB = "table-sync-job";

    /**
     * 数据来源表
     */
    public static final String SOURCE = "source";

    /**
     * 数据来目标表
     */
    public static final String TARGET = "target";

    /**
     * 数据字段映射
     */
    public static final String MAPPING = "mapping";

    /**
     * 删除, 修改的key
     */
    public static final String PRIMARY_KEY = "primary-key";

    /**
     * 执行数据节点
     */
    public static final String EXECUTER = "executer";

    /**
     * 配置实例
     */
    private static Config config;

    /**
     * 数据来源链接
     */
    private static Connection dataSourceConn;

    /**
     * 数据目标链接
     */
    private static Connection dataTargetConn;

    /**
     * 入口
     *
     * @param args
     * @throws SQLException
     * @throws ParseException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, ParseException, IOException, InstantiationException, IllegalAccessException {
        log.info("Main start ... ");
        config = new Config();
        JSONObject configJson = getConf(args);

        //数据源配置
        DataSourceConfig dataSource = configJson.getObject(DATA_SOURCE, DataSourceConfig.class);
        config.setDataSourceConfig(dataSource);

        //目标数据配置
        DataTargetConfig dataTarget = configJson.getObject(DATA_TARGET, DataTargetConfig.class);
        config.setDataTargetConfig(dataTarget);

        dataSourceConn = getConnection(
                dataSource.getHost(), dataSource.getPort(), dataSource.getUsername(), dataSource.getPassword());

        dataTargetConn = getConnection(
                dataTarget.getHost(), dataTarget.getPort(), dataTarget.getUsername(), dataTarget.getPassword());

        //binlog name position
        if (configJson.containsKey(LOG_NAME)) {
            config.setLogName(configJson.getString(LOG_NAME));
        }
        if (configJson.containsKey(LOG_POSITION)) {
            config.setPosition(configJson.getLong(LOG_POSITION));
        }

        //加载配置
        eventHandler(configJson);

        //加载插件
        eventPlugins(configJson);

        for (EventHandler handler : config.getEventHandlers()) {
            //打印日志
            log.debug("init EventHandler: {}", handler);
        }

        //启动
        PlumberLancher plumberLancher = new PlumberLancher();
        Vertx vertx = Vertx.vertx();
        Context context = vertx.getOrCreateContext();
        plumberLancher.setVertx(vertx);
        plumberLancher.setContext(context);
        plumberLancher.start(config);
        log.info("Main start success ...");

    }

    /**
     * 加载插件
     *
     * @param configJson
     * @return
     */
    private static void eventPlugins(JSONObject configJson) throws IllegalAccessException, InstantiationException {
        JSONObject pluginJson = configJson.getJSONObject(EXECUTER);
        List<SqlEventDataExecuter> eventPlugins = new ArrayList<>();
        if (pluginJson != null) {
            for (Map.Entry<String, Object> entry : pluginJson.entrySet()) {
                String pluginName = entry.getKey();
                JSONObject jsonObject = pluginJson.getJSONObject(pluginName);
                Class<? extends SqlEventDataExecuter> eventPluginClass = SqlEventDataExecuter.EVENT_PLUGIN_MAP.getOrDefault(pluginName, MysqlEventExecuter.class);
                SqlEventDataExecuter eventPlugin = eventPluginClass.newInstance();
                eventPlugin.setConfig(jsonObject);
                eventPlugins.add(eventPlugin);
            }
        }
        for (EventHandler handler : config.getEventHandlers()) {
            for (SqlEventDataExecuter eventPlugin : eventPlugins) {
                handler.addPlugin(eventPlugin);
            }
        }
    }

    /**
     * 加载全部的EventHandler
     *
     * @param configJson
     * @throws SQLException
     */
    private static void eventHandler(JSONObject configJson) throws SQLException {
        Set<EventHandler> eventHandlers = new HashSet();
        config.setEventHandlers(eventHandlers);
        //配置了TABLE_SYNC_JOB节点
        if (configJson.containsKey(TABLE_SYNC_JOB)) {
            JSONArray eventHandlerArray = configJson.getJSONArray(TABLE_SYNC_JOB);
            for (int i = 0; i < eventHandlerArray.size(); i++) {
                JSONObject eventHandlerJson = eventHandlerArray.getJSONObject(i);
                EventHandler eventHandler = eventHandlerByJson(eventHandlerJson);
                eventHandlers.add(eventHandler);
            }
            return;
        }
        //没有配置TABLE_SYNC_JOB节点, 默认两库数中的表，结构完全一样，直接全库同步
        DataSourceConfig sourceConfig = config.getDataSourceConfig();
        List<String> tables = tables(dataSourceConn, sourceConfig.getDatabase());
        for (String table : tables) {
            EventHandler eventHandler = eventHandlerByTable(table);
            eventHandlers.add(eventHandler);
        }
    }

    /**
     * 根据表名称加载 EventHandler
     *
     * @param table
     * @return
     */
    private static EventHandler eventHandlerByTable(String table) throws SQLException {
        DataSourceConfig sourceConfig = config.getDataSourceConfig();
        String sourceDatabase = sourceConfig.getDatabase();
        String targetDatabase = config.getDataTargetConfig().getDatabase();
        String createSql = getCreateSql(dataSourceConn, sourceDatabase, table);
        TableMateData source = TableMateDataUtils.getTableMateData(createSql, sourceDatabase);
        TableMateData target = TableMateDataUtils.getTableMateData(createSql, targetDatabase);

        //new
        EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
        eventHandler.setStatus(true);

        //source target
        eventHandler.setSource(source);
        eventHandler.setTarget(target);

        //mapping
        Map<String, String> map = new HashMap<>();
        List<String> columns = source.getColumns();
        for (String column : columns) {
            map.put(column, column);
        }
        eventHandler.setMapping(map);

        //id
        String id = source.getId();
        if (id == null || id.length() == 0) {
            throw new RuntimeException(table + " primary key not find");
        }
        eventHandler.setKey(id);
        return eventHandler;
    }

    /**
     * 根据配置加载 EventHandler
     *
     * @param eventHandlerJson
     * @return
     * @throws SQLException
     */
    private static EventHandler eventHandlerByJson(JSONObject eventHandlerJson) throws SQLException {
        EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
        eventHandler.setStatus(true);
        //source table
        String sourceTable = eventHandlerJson.getString(SOURCE);
        DataSourceConfig dataSourceConfig = config.getDataSourceConfig();
        String sourceSql = getCreateSql(dataSourceConn, dataSourceConfig.getDatabase(), sourceTable);
        TableMateData sourceMateData = TableMateDataUtils.getTableMateData(sourceSql, dataSourceConfig.getDatabase());
        eventHandler.setSource(sourceMateData);

        //target table
        String targetTable = eventHandlerJson.getString(TARGET);
        DataTargetConfig dataTargetConfig = config.getDataTargetConfig();
        String targetSql = getCreateSql(dataTargetConn, dataTargetConfig.getDatabase(), targetTable);
        TableMateData targetMateData = TableMateDataUtils.getTableMateData(targetSql, dataTargetConfig.getDatabase());
        eventHandler.setTarget(targetMateData);

        //mapping
        Map<String, String> map = new HashMap<>();
        eventHandler.setMapping(map);
        //mapping != null , 两个表数据结构不相同( 数据转换同步 )
        if (eventHandlerJson.containsKey(MAPPING)) {
            JSONObject mapping = eventHandlerJson.getJSONObject(MAPPING);
            Set<String> keySet = mapping.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                map.put(key, mapping.getString(key));
            }
        } else {
            //mapping = null , 两个表数据结构相同( 数据直接同步 )
            List<String> columns = targetMateData.getColumns();
            for (String column : columns) {
                map.put(column, column);
            }
        }
        //id如果有配置，按照配置
        if (eventHandlerJson.containsKey(PRIMARY_KEY)) {
            String id = eventHandlerJson.getString(PRIMARY_KEY);
            eventHandler.setKey(id);
        } else {
            //数据来源表中的主键
            String id = targetMateData.getId();
            if (id == null || id.length() == 0) {
                throw new RuntimeException(targetMateData.getNama() + " primary key not find");
            }
            eventHandler.setKey(id);
        }
        return eventHandler;
    }

    /**
     * 获取配置文件
     *
     * @param args
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private static JSONObject getConf(String[] args) throws ParseException, IOException {
        Options options = new Options();
        options.addOption("c", "conf", true, "config file path");
        CommandLine parse = new BasicParser().parse(options, args);
        if (!parse.hasOption("conf")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
            System.exit(0);
        }
        String conf = parse.getOptionValue("conf");
        if (!conf.startsWith(File.separator)) {
            String path = System.getProperty("user.dir");
            conf = path + "/" + conf;
        }
        File file = new File(conf);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        JSONObject configJson = JSONObject.parseObject(new String(bytes, "utf-8"));
        return configJson;
    }

    /**
     * 获取数据库链接
     *
     * @param host
     * @param port
     * @param user
     * @param pwd
     * @return
     * @throws SQLException
     */
    private static Connection getConnection(String host, int port, String user, String pwd) throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "?characterEncoding=utf-8", user, pwd
        );
        return connection;
    }

    /**
     * 获取表的建表sql
     *
     * @param connection
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    private static String getCreateSql(Connection connection, String database, String table) throws SQLException {
        String sql = "show create table " + database + "." + table;
        List<Map<String, Object>> maps = new QueryRunner().query(connection, sql, new MapListHandler());
        Map<String, Object> map = maps.get(0);
        return map.get("Create Table").toString();
    }

    /**
     * 找到数据库中所有的表
     *
     * @param connection
     * @param database
     * @return
     * @throws SQLException
     */
    private static List<String> tables(Connection connection, String database) throws SQLException {
        String sql = "select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = '" + database + "';";
        List<String> list = new ArrayList<>();
        List<Map<String, Object>> maps = new QueryRunner().query(connection, sql, new MapListHandler());
        for (Map<String, Object> map : maps) {
            list.add(map.get("TABLE_NAME").toString());
        }
        return list;
    }

    /**
     * 获取binlogs
     *
     * @param connection
     * @return
     * @throws SQLException
     */
    private static List<BinaryLog> binlogs(Connection connection) throws SQLException {
        String sql = "show binary logs;";
        List<BinaryLog> binlogs = new ArrayList<>();
        List<Map<String, Object>> maps = new QueryRunner().query(connection, sql, new MapListHandler());
        for (Map<String, Object> binlog : maps) {
            String logName = binlog.get("Log_name").toString();
            long fileSize = Long.valueOf(binlog.get("File_size").toString());
            BinaryLog binaryLogs = new BinaryLog();
            binaryLogs.setLogName(logName);
            binaryLogs.setFileSize(fileSize);
            binlogs.add(binaryLogs);
        }
        return binlogs;
    }

    @Data
    static class BinaryLog {
        String logName;
        long fileSize;
    }

}
