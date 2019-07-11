# plumber

数据实时同步工具

### 原理

将自己伪装成 mysql slave，获取mysql推送的binlog信息，通过字段映射，处理成为新的sql并向目标库执从而完成数据实时同步。

### 代码示例

```java

import com.hebaibai.plumber.PlumberApplicationTests;
import com.hebaibai.plumber.DataSourceConfig;
import com.hebaibai.plumber.core.BinaryLogClientService;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class InsertUpdateDeleteEventHandlerImplTest extends PlumberApplicationTests {

    @Autowired
    private BinaryLogClientService binaryLogClientService;

    @Test
    public void handle() throws SQLException, PropertyVetoException {

        //目标数据源
        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1?useUnicode=true&characterEncoding=utf-8");
        comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("root");
        DataSource dataSource = comboPooledDataSource;

        //binlog来源数据库配置
        Auth dataSourceConfig = new Auth();
        dataSourceConfig.setHostname("127.0.0.1");
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("root");

        //创建链接
        binaryLogClientService.create(dataSourceConfig);

        //注册binlog table map 事件 处理器
        EventHandler tableMapEventHandler = new TableMapEventHandlerImpl();
        tableMapEventHandler.setSource(dataSourceConfig, null, null);

        //注册 增删改 事件处理器
        EventHandler eventHandler = new InsertUpdateDeleteEventHandlerImpl();
        eventHandler.setSource(dataSourceConfig, "testdb", "table_1");
        eventHandler.setTarget(dataSource, "testdb", "table_2");
        eventHandler.setStatus(true);

        //添加字段映射信息
        eventHandler.setKeys(new HashSet<String>() {{
            add("id");
        }});
        eventHandler.setMapping(new HashMap<String, String>() {{
            put("id", "id_2");
            put("name", "name_2");
            put("namespace", "namespace_2");
            put("dal_group_id", "dal_group_id_2");
            put("dal_config_name", "dal_config_name_2");
            put("update_user_no", "update_user_no_2");
            put("update_time", "update_time_2");
        }});
        binaryLogClientService.registEventHandler(dataSourceConfig, tableMapEventHandler);
        binaryLogClientService.registEventHandler(dataSourceConfig, eventHandler);

        //启动链接
        binaryLogClientService.start(dataSourceConfig);

        //维持测试代码运行
        while (true) {

        }
    }
}
```
