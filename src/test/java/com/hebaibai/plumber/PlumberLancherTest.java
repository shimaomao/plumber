package com.hebaibai.plumber;

import com.hebaibai.plumber.core.handler.EventHandler;
import com.hebaibai.plumber.core.handler.InsertUpdateDeleteEventHandlerImpl;
import com.hebaibai.plumber.core.utils.TableMateData;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class PlumberLancherTest {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();

        //数据来源
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setHostname("172.31.100.51");
        dataSourceConfig.setPort(3306);
        dataSourceConfig.setUsername("pubmidb");
        dataSourceConfig.setPassword("Pubmi@2016");
        config.setDataSourceConfig(dataSourceConfig);

        //数据目标
        DataTargetConfig dataTargetConfig = new DataTargetConfig();
        dataTargetConfig.setCharset("utf-8");
        dataTargetConfig.setHost("172.31.100.51");
        dataTargetConfig.setPort(3306);
        dataTargetConfig.setUsername("pubmidb");
        dataTargetConfig.setPassword("Pubmi@2016");
        config.setDataTargetConfig(dataTargetConfig);

        //增删改处理器
        EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
        //数据字段映射，key：来源字段，value：目标字段
        eventHandler.setMapping(new HashMap() {{
            put("id", "id_2");
            put("name", "name_2");
            put("namespace", "namespace_2");
            put("dal_group_id", "dal_group_id_2");
            put("dal_config_name", "dal_config_name_2");
            put("update_user_no", "update_user_no_2");
            put("update_time", "update_time_2");
        }});
        //设置来源表的key
        eventHandler.setKeys(new HashSet() {{
            add("id");
        }});
        eventHandler.setStatus(true);
        //来源表信息
        eventHandler.setSource(new TableMateData() {{
            //表名
            setNama("project");
            //数据库名
            setDataBase("pubmidb");
            //表中的列，顺序要和建表语句中一致
            setColumns(new ArrayList() {{
                add("id");
                add("name");
                add("namespace");
                add("dal_group_id");
                add("dal_config_name");
                add("update_user_no");
                add("update_time");
            }});
        }});
        //目标表
        eventHandler.setTarget(new TableMateData() {{
            setNama("project_2");
            setDataBase("pubmidb");
            setColumns(new ArrayList() {{
                add("id_2");
                add("name_2");
                add("namespace_2");
                add("dal_group_id_2");
                add("dal_config_name_2");
                add("update_user_no_2");
                add("update_time_2");
            }});
        }});
        config.setEventHandlers(new HashSet() {{
            add(eventHandler);
        }});
        //启动前配置
        Vertx vertx = Vertx.vertx();
        Context context = vertx.getOrCreateContext();
        PlumberLancher plumberLancher = new PlumberLancher(config);
        plumberLancher.setVertx(vertx);
        plumberLancher.setContext(context);
        //启动
        plumberLancher.start();
        for (String verticleId : plumberLancher.verticleIds()) {
            System.out.println(verticleId);
        }
        TimeUnit.SECONDS.sleep(5);
        //停止
        plumberLancher.stop();
        for (String verticleId : plumberLancher.verticleIds()) {
            System.out.println(verticleId);
        }
    }
}
