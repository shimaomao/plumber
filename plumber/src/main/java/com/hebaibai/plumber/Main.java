package com.hebaibai.plumber;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.handler.InsertUpdateDeleteEventHandlerImpl;
import com.hebaibai.plumber.core.utils.TableMateData;
import com.hebaibai.plumber.core.utils.TableMateDataUtils;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.logging.JULLogDelegateFactory;
import io.vertx.core.spi.logging.LogDelegate;
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
public class Main {

    static {
        JULLogDelegateFactory.loadConfig();
    }

    static LogDelegate log = new JULLogDelegateFactory().createDelegate(Main.class.getName());

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
    public static final String KEYS = "keys";


    public static void main(String[] args) {
        log.info("Main start ... ");
        Config config = new Config();
        try (FileInputStream inputStream = new FileInputStream(getConf(args))) {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            JSONObject jsonObject = JSONObject.parseObject(new String(bytes, "utf-8"));
            //binlog name position
            String logName = jsonObject.getString(LOG_NAME);
            if (logName != null) {
                config.setLogName(logName);
                Long position = jsonObject.getLong(LOG_POSITION);
                if (position != null) {
                    config.setPosition(position);
                }
            }
            //数据源配置
            DataSourceConfig dataSource = jsonObject.getObject(DATA_SOURCE, DataSourceConfig.class);
            config.setDataSourceConfig(dataSource);
            //目标数据配置
            DataTargetConfig dataTarget = jsonObject.getObject(DATA_TARGET, DataTargetConfig.class);
            config.setDataTargetConfig(dataTarget);

            Connection dataSourceConn = getConnection(
                    dataSource.getHost(), dataSource.getPort(), dataSource.getUsername(), dataSource.getPassword());

            Connection dataTargetConn = getConnection(
                    dataTarget.getHost(), dataTarget.getPort(), dataTarget.getUsername(), dataTarget.getPassword());

            //eventHandler
            JSONArray eventHandlerArray = jsonObject.getJSONArray(TABLE_SYNC_JOB);
            Set<EventHandler> eventHandlers = new HashSet();
            config.setEventHandlers(eventHandlers);
            for (int i = 0; i < eventHandlerArray.size(); i++) {
                EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
                eventHandler.setStatus(true);
                JSONObject eventHandlerJson = eventHandlerArray.getJSONObject(i);
                //source table
                String sourceTable = eventHandlerJson.getString(SOURCE);
                String sourceSql = getCreateSql(dataSourceConn, dataSource.getDatabase(), sourceTable);
                TableMateData sourceMateData = TableMateDataUtils.getTableMateData(sourceSql, dataSource.getDatabase());
                eventHandler.setSource(sourceMateData);
                //target table
                String targetTable = eventHandlerJson.getString(TARGET);
                String targetSql = getCreateSql(dataTargetConn, dataTarget.getDatabase(), targetTable);
                TableMateData targetMateData = TableMateDataUtils.getTableMateData(targetSql, dataTarget.getDatabase());
                eventHandler.setTarget(targetMateData);
                //mapping
                JSONObject mapping = eventHandlerJson.getJSONObject(MAPPING);
                //mapping != null , 两个表数据结构不相同( 数据转换同步 )
                if (mapping != null) {
                    Map<String, String> map = new HashMap<>();
                    Set<String> keySet = mapping.keySet();
                    Iterator<String> iterator = keySet.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        map.put(key, mapping.getString(key));
                    }
                    eventHandler.setMapping(map);
                }
                //mapping = null , 两个表数据结构相同( 数据直接同步 )
                else {
                    List<String> columns = targetMateData.getColumns();
                    Map<String, String> map = new HashMap<>();
                    for (String column : columns) {
                        map.put(column, column);
                    }
                    eventHandler.setMapping(map);
                }
                //keys
                JSONArray keysJson = eventHandlerJson.getJSONArray(KEYS);
                Set<String> keys = new HashSet<>();
                for (int j = 0; j < keysJson.size(); j++) {
                    String key = keysJson.getString(j);
                    keys.add(key);
                }
                eventHandler.setKeys(keys);

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
