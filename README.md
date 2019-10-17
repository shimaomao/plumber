# plumber

数据实时同步工具，使用 vert.x 异步框架，效率高


### 原理

将自己伪装成 mysql slave，获取mysql推送的binlog信息，通过字段映射，处理成为新的sql并向目标库执行,从而完成数据实时同步。

### 使用条件

mysql开启binlog记录, 格式为ROW

### 安装启动说明

1. git clone https://github.com/hjx601496320/plumber.git
2. cd plumber/plumber/
3. mvn package
4. cd target/
5. tar -zxvf plumber.tar.gz
6. cd plumber/ 并修改config/config.json
7. 执行 sh bin/start.sh 启动

#### 注意:

启动脚本目前只支持linux系统, win脚本不会写 /(ㄒoㄒ)/~~

### 节点说明:

-   **log-name**:binlog 名称(默认为最新文件)
-   **log-position**:binlog 开始位置(默认为后位置)
-   **data-source**:数据来源
-   **executer**:数据持久化执行工具配置, **目前只支持mysql**, 后续添加更多
    -   **MysqlEventExecuter**: mysql的持久化工具
        -   **data-target**:数据目标配置
-   **table-sync-job**:事件处理, 数组 (不配置的话整个库同步,认为表结构完全一致)
    -    **primary-key**:更新,删除条件字段. 默认数据库id.
    -    **source**:数据来源库的表
    -    **target**:数据目标库的表
    -    **mapping**:字段映射. key:来源表字段名称, value:目标表字段名称. (必须包含primary-key, 例如primary-key为"tableId", 那么mapping 中必须包含"tableId")

### 1:直接同步数据 ( 不转换数据 )

```json
{
    "log-name":null,
    "log-position":null,
    "data-source":{
        "host":"127.0.0.1",
        "database":"dbname",
        "port":3306,
        "username":"root",
        "password":"00000"
    },
    "executer":{
        "MysqlEventExecuter":{
            "data-target":{
                "host":"127.0.0.1",
                "database":"dbname",
                "port":3306,
                "username":"root",
                "password":"00000"
            }
        }
    },
    "table-sync-job":[
        {
            "primary-key":"id",
            "source":"test_1",
            "target":"test_2"
        }
    ]
}
```

### 2:转换同步数据 ( 转换表名,字段名  )
````json
{
    "log-name":null,
    "log-position":null,
    "data-source":{
        "host":"127.0.0.1",
        "database":"dbname",
        "port":3306,
        "username":"root",
        "password":"00000"
    },
    "executer":{
        "MysqlEventExecuter":{
            "data-target":{
                "host":"127.0.0.1",
                "database":"dbname",
                "port":3306,
                "username":"root",
                "password":"00000"
            }
        }
    },
    "table-sync-job":[
        {
            "mapping":{
                "id":"id_2",
                "name":"name_2",
                "age":"age_2"
            },
            "primary-key":"id",
            "source":"test_1",
            "target":"test_2"
        }
    ]
}
````

### 3:同步整个数据库 (认为两个库的表，字段一致)
```json
{
    "log-name":null,
    "log-position":null,
    "data-source":{
        "host":"127.0.0.1",
        "database":"dbname",
        "port":3306,
        "username":"root",
        "password":"00000"
    },
    "executer":{
        "MysqlEventExecuter":{
            "data-target":{
                "host":"127.0.0.1",
                "database":"dbname",
                "port":3306,
                "username":"root",
                "password":"00000"
            }
        }
    }
}
```

