# plumber

数据实时同步工具，使用 vert.x 异步框架，效率高

### 更新

添加了管理平台页面,可以在页面直接操作.

### 原理

将自己伪装成 mysql slave，获取mysql推送的binlog信息，通过字段映射，处理成为新的sql并向目标库执从而完成数据实时同步。

### 代码示例

```java

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
        dataSourceConfig.setHostname("127.0.0.1");
        dataSourceConfig.setPort(3306);
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");
        config.setDataSourceConfig(dataSourceConfig);

        //数据目标
        DataTargetConfig dataTargetConfig = new DataTargetConfig();
        dataTargetConfig.setCharset("utf-8");
        dataTargetConfig.setHost("127.0.0.1");
        dataTargetConfig.setPort(3306);
        dataTargetConfig.setUsername("root");
        dataTargetConfig.setPassword("root");
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
        TimeUnit.SECONDS.sleep(10);
        //停止
        plumberLancher.start();
    }
}

```
