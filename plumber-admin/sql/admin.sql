-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: admin
-- ------------------------------------------------------
-- Server version	5.7.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `b_column_key`
--

DROP TABLE IF EXISTS `b_column_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_column_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_mapping_id` int(11) DEFAULT NULL COMMENT 'data_mapping表id',
  `key_name` varchar(100) DEFAULT NULL COMMENT '键名称',
  `create_time` datetime DEFAULT NULL,
  `mark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='键';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_column_key`
--

LOCK TABLES `b_column_key` WRITE;
/*!40000 ALTER TABLE `b_column_key` DISABLE KEYS */;
/*!40000 ALTER TABLE `b_column_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b_column_mapping`
--

DROP TABLE IF EXISTS `b_column_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_column_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_mapping_id` int(11) DEFAULT NULL COMMENT 'data_mapping表id',
  `target` varchar(100) DEFAULT NULL COMMENT '目标名称',
  `source` varchar(100) DEFAULT NULL COMMENT '来源名称',
  `target_type` varchar(100) DEFAULT NULL COMMENT '目标数据类型',
  `source_type` varchar(100) DEFAULT NULL COMMENT '来源数据类型',
  `order` int(11) DEFAULT NULL COMMENT '从小到达排序',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COMMENT='字段映射';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_column_mapping`
--

LOCK TABLES `b_column_mapping` WRITE;
/*!40000 ALTER TABLE `b_column_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `b_column_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b_data_config`
--

DROP TABLE IF EXISTS `b_data_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_data_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(100) DEFAULT NULL,
  `user` varchar(100) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `pwd` varchar(100) DEFAULT NULL,
  `mark` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '1:source; 0:target',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='数据目标';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_data_config`
--

LOCK TABLES `b_data_config` WRITE;
/*!40000 ALTER TABLE `b_data_config` DISABLE KEYS */;
INSERT INTO `b_data_config` VALUES (1,'127.0.0.1','demo',3306,'root','sit','2019-07-10 02:10:38',1),(2,'127.0.0.1','demo',3306,'root','sit','2019-07-10 02:10:38',0);
/*!40000 ALTER TABLE `b_data_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b_data_mapping`
--

DROP TABLE IF EXISTS `b_data_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_data_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plumber_id` int(11) DEFAULT NULL,
  `source_database` varchar(100) DEFAULT NULL COMMENT '来源数据库',
  `source_table` varchar(100) DEFAULT NULL COMMENT '来源表',
  `target_database` varchar(100) DEFAULT NULL COMMENT '目标数据库',
  `target_table` varchar(100) DEFAULT NULL COMMENT '目标表',
  `mark` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='数据映射';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_data_mapping`
--

LOCK TABLES `b_data_mapping` WRITE;
/*!40000 ALTER TABLE `b_data_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `b_data_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `b_plumber`
--

DROP TABLE IF EXISTS `b_plumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `b_plumber` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `data_source_id` int(11) DEFAULT NULL,
  `data_target_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `last_run` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COMMENT='数据同步';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `b_plumber`
--

LOCK TABLES `b_plumber` WRITE;
/*!40000 ALTER TABLE `b_plumber` DISABLE KEYS */;
INSERT INTO `b_plumber` VALUES (12,'sit-sit',1,2,'2019-07-16 15:00:25',1,'2019-07-17 17:55:33');
/*!40000 ALTER TABLE `b_plumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_dept`
--

DROP TABLE IF EXISTS `t_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_dept` (
  `DEPT_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级部门ID',
  `DEPT_NAME` varchar(100) NOT NULL COMMENT '部门名称',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`DEPT_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_dept`
--

LOCK TABLES `t_dept` WRITE;
/*!40000 ALTER TABLE `t_dept` DISABLE KEYS */;
INSERT INTO `t_dept` VALUES (1,0,'开发部',1,'2019-06-14 20:56:41',NULL),(2,1,'开发一部',1,'2019-06-14 20:58:46',NULL),(3,1,'开发二部',2,'2019-06-14 20:58:56',NULL);
/*!40000 ALTER TABLE `t_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_eximport`
--

DROP TABLE IF EXISTS `t_eximport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_eximport` (
  `FIELD1` varchar(20) NOT NULL COMMENT '字段1',
  `FIELD2` int(11) NOT NULL COMMENT '字段2',
  `FIELD3` varchar(100) NOT NULL COMMENT '字段3',
  `CREATE_TIME` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='Excel导入导出测试';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_eximport`
--

LOCK TABLES `t_eximport` WRITE;
/*!40000 ALTER TABLE `t_eximport` DISABLE KEYS */;
INSERT INTO `t_eximport` VALUES ('字段1',1,'mrbird0@gmail.com','2019-06-13 03:14:06'),('字段1',2,'mrbird1@gmail.com','2019-06-13 03:14:06'),('字段1',3,'mrbird2@gmail.com','2019-06-13 03:14:06'),('字段1',4,'mrbird3@gmail.com','2019-06-13 03:14:06'),('字段1',5,'mrbird4@gmail.com','2019-06-13 03:14:06'),('字段1',6,'mrbird5@gmail.com','2019-06-13 03:14:06'),('字段1',7,'mrbird6@gmail.com','2019-06-13 03:14:06'),('字段1',8,'mrbird7@gmail.com','2019-06-13 03:14:06'),('字段1',9,'mrbird8@gmail.com','2019-06-13 03:14:06'),('字段1',10,'mrbird9@gmail.com','2019-06-13 03:14:06'),('字段1',11,'mrbird10@gmail.com','2019-06-13 03:14:06'),('字段1',12,'mrbird11@gmail.com','2019-06-13 03:14:06'),('字段1',13,'mrbird12@gmail.com','2019-06-13 03:14:06'),('字段1',14,'mrbird13@gmail.com','2019-06-13 03:14:06'),('字段1',15,'mrbird14@gmail.com','2019-06-13 03:14:06'),('字段1',16,'mrbird15@gmail.com','2019-06-13 03:14:06'),('字段1',17,'mrbird16@gmail.com','2019-06-13 03:14:06'),('字段1',18,'mrbird17@gmail.com','2019-06-13 03:14:06'),('字段1',19,'mrbird18@gmail.com','2019-06-13 03:14:06'),('字段1',20,'mrbird19@gmail.com','2019-06-13 03:14:06');
/*!40000 ALTER TABLE `t_eximport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_generator_config`
--

DROP TABLE IF EXISTS `t_generator_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_generator_config` (
  `id` int(11) NOT NULL COMMENT '主键',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `base_package` varchar(50) NOT NULL COMMENT '基础包名',
  `entity_package` varchar(20) NOT NULL COMMENT 'entity文件存放路径',
  `mapper_package` varchar(20) NOT NULL COMMENT 'mapper文件存放路径',
  `mapper_xml_package` varchar(20) NOT NULL COMMENT 'mapper xml文件存放路径',
  `service_package` varchar(20) NOT NULL COMMENT 'servcie文件存放路径',
  `service_impl_package` varchar(20) NOT NULL COMMENT 'serviceImpl文件存放路径',
  `controller_package` varchar(20) NOT NULL COMMENT 'controller文件存放路径',
  `is_trim` char(1) NOT NULL COMMENT '是否去除前缀 1是 0否',
  `trim_value` varchar(10) DEFAULT NULL COMMENT '前缀内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='代码生成配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_generator_config`
--

LOCK TABLES `t_generator_config` WRITE;
/*!40000 ALTER TABLE `t_generator_config` DISABLE KEYS */;
INSERT INTO `t_generator_config` VALUES (1,'hejiaxuan','com.hebaibai.admin.plumber','entity','mapper','mapper','service','service.impl','controller','1','b_s');
/*!40000 ALTER TABLE `t_generator_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_job`
--

DROP TABLE IF EXISTS `t_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_job` (
  `JOB_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `BEAN_NAME` varchar(50) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(50) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(50) DEFAULT NULL COMMENT '参数',
  `CRON_EXPRESSION` varchar(20) NOT NULL COMMENT 'cron表达式',
  `STATUS` char(2) NOT NULL COMMENT '任务状态  0：正常  1：暂停',
  `REMARK` varchar(50) DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`JOB_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='定时任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_job`
--

LOCK TABLES `t_job` WRITE;
/*!40000 ALTER TABLE `t_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_job_log`
--

DROP TABLE IF EXISTS `t_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_job_log` (
  `LOG_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
  `JOB_ID` bigint(20) NOT NULL COMMENT '任务id',
  `BEAN_NAME` varchar(100) NOT NULL COMMENT 'spring bean名称',
  `METHOD_NAME` varchar(100) NOT NULL COMMENT '方法名',
  `PARAMS` varchar(200) DEFAULT NULL COMMENT '参数',
  `STATUS` char(2) NOT NULL COMMENT '任务状态    0：成功    1：失败',
  `ERROR` text COMMENT '失败信息',
  `TIMES` decimal(11,0) DEFAULT NULL COMMENT '耗时(单位：毫秒)',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2563 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_job_log`
--

LOCK TABLES `t_job_log` WRITE;
/*!40000 ALTER TABLE `t_job_log` DISABLE KEYS */;
INSERT INTO `t_job_log` VALUES (2472,2,'testTask','test1',NULL,'0',NULL,14,'2018-04-02 17:29:50'),(2473,2,'testTask','test1',NULL,'0',NULL,1,'2018-04-02 17:30:00'),(2474,2,'testTask','test1',NULL,'0',NULL,0,'2018-04-02 17:30:10'),(2475,2,'testTask','test1',NULL,'0',NULL,1,'2018-04-02 17:30:20'),(2476,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 08:49:24'),(2477,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 08:49:25'),(2478,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 08:49:26'),(2479,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 08:49:27'),(2480,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 08:49:28'),(2481,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 08:49:29'),(2482,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 08:49:30'),(2483,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 08:49:31'),(2484,1,'testTask','test','mrbird','0',NULL,4,'2019-06-11 10:43:36'),(2485,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:37'),(2486,1,'testTask','test','mrbird','0',NULL,15,'2019-06-11 10:43:38'),(2487,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:39'),(2488,1,'testTask','test','mrbird','0',NULL,53,'2019-06-11 10:43:40'),(2489,1,'testTask','test','mrbird','0',NULL,3,'2019-06-11 10:43:41'),(2490,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:43:42'),(2491,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:43'),(2492,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:43:44'),(2493,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:45'),(2494,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:46'),(2495,1,'testTask','test','mrbird','0',NULL,13,'2019-06-11 10:43:47'),(2496,1,'testTask','test','mrbird','0',NULL,35,'2019-06-11 10:43:48'),(2497,1,'testTask','test','mrbird','0',NULL,3,'2019-06-11 10:43:49'),(2498,1,'testTask','test','mrbird','0',NULL,7,'2019-06-11 10:43:50'),(2499,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:51'),(2500,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:52'),(2501,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:43:53'),(2502,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:54'),(2503,1,'testTask','test','mrbird','0',NULL,14,'2019-06-11 10:43:55'),(2504,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:43:56'),(2505,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:57'),(2506,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:43:58'),(2507,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:43:59'),(2508,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:44:00'),(2509,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:01'),(2510,1,'testTask','test','mrbird','0',NULL,37,'2019-06-11 10:44:02'),(2511,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:03'),(2512,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:04'),(2513,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:05'),(2514,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:44:06'),(2515,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:44:07'),(2516,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:08'),(2517,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:09'),(2518,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:10'),(2519,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:11'),(2520,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:12'),(2521,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:13'),(2522,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:16'),(2523,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:16'),(2524,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:16'),(2525,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:17'),(2526,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:18'),(2527,1,'testTask','test','mrbird','0',NULL,145,'2019-06-11 10:44:19'),(2528,1,'testTask','test','mrbird','0',NULL,11,'2019-06-11 10:44:20'),(2529,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:21'),(2530,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:22'),(2531,1,'testTask','test','mrbird','0',NULL,206,'2019-06-11 10:44:23'),(2532,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:44:24'),(2533,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:44:25'),(2534,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:26'),(2535,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:44:27'),(2536,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:44:28'),(2537,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:45:54'),(2538,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:45:55'),(2539,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:45:56'),(2540,1,'testTask','test','mrbird','0',NULL,4,'2019-06-11 10:45:57'),(2541,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:45:58'),(2542,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:45:59'),(2543,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:46:00'),(2544,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:01'),(2545,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:02'),(2546,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:46:03'),(2547,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:04'),(2548,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:46:05'),(2549,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:46:06'),(2550,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:07'),(2551,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:46:45'),(2552,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:46'),(2553,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:47'),(2554,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:46:48'),(2555,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:49'),(2556,1,'testTask','test','mrbird','0',NULL,10,'2019-06-11 10:46:50'),(2557,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:46:51'),(2558,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:52'),(2559,1,'testTask','test','mrbird','0',NULL,2,'2019-06-11 10:46:53'),(2560,1,'testTask','test','mrbird','0',NULL,0,'2019-06-11 10:46:54'),(2561,1,'testTask','test','mrbird','0',NULL,1,'2019-06-11 10:46:55'),(2562,11,'testTask','test2',NULL,'1','java.lang.NoSuchMethodException: cc.mrbird.febs.job.task.TestTask.test2()',3,'2019-07-08 13:06:44');
/*!40000 ALTER TABLE `t_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_log`
--

DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_log` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '操作用户',
  `OPERATION` text COMMENT '操作内容',
  `TIME` decimal(11,0) DEFAULT NULL COMMENT '耗时',
  `METHOD` text COMMENT '操作方法',
  `PARAMS` text COMMENT '方法参数',
  `IP` varchar(64) DEFAULT NULL COMMENT '操作者IP',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `location` varchar(50) DEFAULT NULL COMMENT '操作地点',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1185 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_log`
--

LOCK TABLES `t_log` WRITE;
/*!40000 ALTER TABLE `t_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_login_log`
--

DROP TABLE IF EXISTS `t_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_login_log` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `LOGIN_TIME` datetime NOT NULL COMMENT '登录时间',
  `LOCATION` varchar(50) DEFAULT NULL COMMENT '登录地点',
  `IP` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `SYSTEM` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `BROWSER` varchar(50) DEFAULT NULL COMMENT '浏览器',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_login_log`
--

LOCK TABLES `t_login_log` WRITE;
/*!40000 ALTER TABLE `t_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_menu`
--

DROP TABLE IF EXISTS `t_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_menu` (
  `MENU_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单/按钮ID',
  `PARENT_ID` bigint(20) NOT NULL COMMENT '上级菜单ID',
  `MENU_NAME` varchar(50) NOT NULL COMMENT '菜单/按钮名称',
  `URL` varchar(50) DEFAULT NULL COMMENT '菜单URL',
  `PERMS` text COMMENT '权限标识',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `TYPE` char(2) NOT NULL COMMENT '类型 0菜单 1按钮',
  `ORDER_NUM` bigint(20) DEFAULT NULL COMMENT '排序',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_menu`
--

LOCK TABLES `t_menu` WRITE;
/*!40000 ALTER TABLE `t_menu` DISABLE KEYS */;
INSERT INTO `t_menu` VALUES (1,0,'系统管理',NULL,NULL,'layui-icon-setting','0',1,'2017-12-27 16:39:07',NULL),(2,0,'系统监控','','','layui-icon-alert','0',2,'2017-12-27 16:45:51','2019-06-13 11:20:40'),(3,1,'用户管理','/system/user','user:view','layui-icon-meh','0',1,'2017-12-27 16:47:13','2019-06-13 11:13:55'),(4,1,'角色管理','/system/role','role:view','','0',2,'2017-12-27 16:48:09','2019-06-13 08:57:19'),(5,1,'菜单管理','/system/menu','menu:view','','0',3,'2017-12-27 16:48:57','2019-06-13 08:57:34'),(6,1,'部门管理','/system/dept','dept:view','','0',4,'2017-12-27 16:57:33','2019-06-14 19:56:00'),(8,2,'在线用户','/monitor/online','online:view','','0',1,'2017-12-27 16:59:33','2019-06-13 14:30:31'),(10,2,'系统日志','/monitor/log','log:view','','0',2,'2017-12-27 17:00:50','2019-06-13 14:30:37'),(11,3,'新增用户',NULL,'user:add',NULL,'1',NULL,'2017-12-27 17:02:58',NULL),(12,3,'修改用户',NULL,'user:update',NULL,'1',NULL,'2017-12-27 17:04:07',NULL),(13,3,'删除用户',NULL,'user:delete',NULL,'1',NULL,'2017-12-27 17:04:58',NULL),(14,4,'新增角色',NULL,'role:add',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(15,4,'修改角色',NULL,'role:update',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(16,4,'删除角色',NULL,'role:delete',NULL,'1',NULL,'2017-12-27 17:06:38',NULL),(17,5,'新增菜单',NULL,'menu:add',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(18,5,'修改菜单',NULL,'menu:update',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(19,5,'删除菜单',NULL,'menu:delete',NULL,'1',NULL,'2017-12-27 17:08:02',NULL),(20,6,'新增部门',NULL,'dept:add',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(21,6,'修改部门',NULL,'dept:update',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(22,6,'删除部门',NULL,'dept:delete',NULL,'1',NULL,'2017-12-27 17:09:24',NULL),(23,8,'踢出用户',NULL,'user:kickout',NULL,'1',NULL,'2017-12-27 17:11:13',NULL),(24,10,'删除日志',NULL,'log:delete',NULL,'1',NULL,'2017-12-27 17:11:45','2019-06-06 05:56:40'),(113,2,'Redis监控','/monitor/redis/info','redis:view','','0',4,'2018-06-28 14:29:42','2019-06-13 14:30:45'),(114,2,'Redis终端','/monitor/redis/terminal','redis:terminal:view','','0',5,'2018-06-28 15:35:21','2019-06-13 14:30:54'),(115,0,'其他模块',NULL,NULL,'layui-icon-gift','0',5,'2019-05-27 10:18:07',NULL),(116,115,'Apex图表','','',NULL,'0',2,'2019-05-27 10:21:35',NULL),(117,116,'线性图表','/others/apex/line','apex:line:view',NULL,'0',1,'2019-05-27 10:24:49',NULL),(118,115,'高德地图','/others/map','map:view','','0',3,'2019-05-27 17:13:12','2019-06-12 15:33:00'),(119,116,'面积图表','/others/apex/area','apex:area:view',NULL,'0',2,'2019-05-27 18:49:22',NULL),(120,116,'柱形图表','/others/apex/column','apex:column:view',NULL,'0',3,'2019-05-27 18:51:33',NULL),(121,116,'雷达图表','/others/apex/radar','apex:radar:view',NULL,'0',4,'2019-05-27 18:56:05',NULL),(122,116,'条形图表','/others/apex/bar','apex:bar:view',NULL,'0',5,'2019-05-27 18:57:02',NULL),(123,116,'混合图表','/others/apex/mix','apex:mix:view','','0',6,'2019-05-27 18:58:04','2019-06-06 02:55:23'),(125,115,'导入导出','/others/eximport','others:eximport:view','','0',4,'2019-05-27 19:01:57','2019-06-13 01:20:14'),(126,132,'系统图标','/others/admin/icon','admin:icons:view','','0',4,'2019-05-27 19:03:18','2019-07-09 17:27:03'),(127,2,'请求追踪','/monitor/httptrace','httptrace:view','','0',6,'2019-05-27 19:06:38','2019-06-13 14:36:43'),(128,2,'系统信息',NULL,NULL,NULL,'0',7,'2019-05-27 19:08:23',NULL),(129,128,'JVM信息','/monitor/jvm','jvm:view','','0',1,'2019-05-27 19:08:50','2019-06-13 14:36:51'),(130,128,'Tomcat信息','/monitor/tomcat','tomcat:view','','0',2,'2019-05-27 19:09:26','2019-06-13 14:36:57'),(131,128,'服务器信息','/monitor/server','server:view','','0',3,'2019-05-27 19:10:07','2019-06-13 14:37:04'),(132,115,'FEBS组件','','',NULL,'0',1,'2019-05-27 19:13:54',NULL),(133,132,'表单组件','/others/admin/form','admin:form:view','','0',1,'2019-05-27 19:14:45','2019-07-09 17:26:15'),(134,132,'FEBS工具','/others/admin/tools','admin:tools:view','','0',3,'2019-05-29 10:11:22','2019-07-09 17:26:52'),(135,132,'表单组合','/others/admin/form/group','admin:formgroup:view','','0',2,'2019-05-29 10:16:19','2019-07-09 17:26:41'),(136,2,'登录日志','/monitor/loginlog','loginlog:view','','0',3,'2019-05-29 15:56:15','2019-06-13 14:35:56'),(137,0,'代码生成','',NULL,'layui-icon-verticalright','0',4,'2019-06-03 15:35:58',NULL),(138,137,'生成配置','/generator/configure','generator:configure:view',NULL,'0',1,'2019-06-03 15:38:36',NULL),(139,137,'代码生成','/generator/generator','generator:view','','0',2,'2019-06-03 15:39:15','2019-06-13 14:31:38'),(159,132,'其他组件','/others/admin/others','others:admin:others','','0',5,'2019-06-12 07:51:08','2019-07-09 17:27:24'),(160,3,'密码重置',NULL,'user:password:reset',NULL,'1',NULL,'2019-06-13 08:40:13',NULL),(161,3,'导出Excel',NULL,'user:export',NULL,'1',NULL,'2019-06-13 08:40:34',NULL),(162,4,'导出Excel',NULL,'role:export',NULL,'1',NULL,'2019-06-13 14:29:00','2019-06-13 14:29:11'),(163,5,'导出Excel',NULL,'menu:export',NULL,'1',NULL,'2019-06-13 14:29:32',NULL),(164,6,'导出Excel',NULL,'dept:export',NULL,'1',NULL,'2019-06-13 14:29:59',NULL),(165,138,'修改配置',NULL,'generator:configure:update',NULL,'1',NULL,'2019-06-13 14:32:09','2019-06-13 14:32:20'),(166,139,'生成代码',NULL,'generator:generate',NULL,'1',NULL,'2019-06-13 14:32:51',NULL),(167,125,'模板下载',NULL,'eximport:template',NULL,'1',NULL,'2019-06-13 14:33:37',NULL),(168,125,'导出Excel',NULL,'eximport:export',NULL,'1',NULL,'2019-06-13 14:33:57',NULL),(169,125,'导入Excel',NULL,'eximport:import',NULL,'1',NULL,'2019-06-13 14:34:19',NULL),(170,10,'导出Excel',NULL,'log:export',NULL,'1',NULL,'2019-06-13 14:34:55',NULL),(171,136,'删除日志',NULL,'loginlog:delete',NULL,'1',NULL,'2019-06-13 14:35:27','2019-06-13 14:36:08'),(172,136,'导出Excel',NULL,'loginlog:export',NULL,'1',NULL,'2019-06-13 14:36:26',NULL),(177,0,'数据同步','','binlogAuth:view','layui-icon-sync','0',4,'2019-07-08 14:47:03','2019-07-08 16:46:50'),(179,177,'Plumber实例','/plumber','plumber:list','layui-icon-database','0',NULL,'2019-07-08 14:50:36','2019-07-12 17:14:34'),(180,179,'新增',NULL,'plumber:add',NULL,'1',NULL,'2019-07-09 18:02:04','2019-07-12 17:14:41'),(181,179,'更新',NULL,'plumber:update',NULL,'1',NULL,'2019-07-09 18:02:24','2019-07-12 17:14:46'),(182,179,'删除',NULL,'plumber:delete',NULL,'1',NULL,'2019-07-09 18:03:42','2019-07-12 17:14:52'),(184,177,'数据库配置','/dataConfig','dataConfig:list','layui-icon-swap','0',NULL,'2019-07-10 10:02:55','2019-07-12 17:13:08'),(185,184,'新增',NULL,'dataConfig:add',NULL,'1',NULL,'2019-07-10 10:03:59','2019-07-12 17:13:30'),(186,184,'更新',NULL,'dataConfig:update',NULL,'1',NULL,'2019-07-10 10:04:24','2019-07-12 17:13:36'),(187,184,'删除',NULL,'dataConfig:delete',NULL,'1',NULL,'2019-07-10 10:04:57','2019-07-12 17:13:43'),(192,179,'停止',NULL,'plumber:stop',NULL,'1',NULL,'2019-07-15 11:21:10',NULL),(193,179,'发布',NULL,'plumber:publish',NULL,'1',NULL,'2019-07-15 11:21:38',NULL),(194,179,'启动',NULL,'plumber:run',NULL,'1',NULL,'2019-07-15 11:21:57',NULL),(196,177,'映射管理','/dataMapping','dataMapping:list','layui-icon-earth','0',NULL,'2019-07-16 17:09:43',NULL),(197,196,'新增',NULL,'dataMapping:add',NULL,'1',NULL,'2019-07-16 17:17:28',NULL),(198,196,'删除',NULL,'dataMapping:delete',NULL,'1',NULL,'2019-07-17 16:13:50',NULL);
/*!40000 ALTER TABLE `t_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role`
--

DROP TABLE IF EXISTS `t_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role` (
  `ROLE_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `ROLE_NAME` varchar(100) NOT NULL COMMENT '角色名称',
  `REMARK` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role`
--

LOCK TABLES `t_role` WRITE;
/*!40000 ALTER TABLE `t_role` DISABLE KEYS */;
INSERT INTO `t_role` VALUES (1,'系统管理员','系统管理员，拥有所有操作权限 ^_^','2019-06-14 16:23:11','2019-07-17 16:13:58');
/*!40000 ALTER TABLE `t_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_role_menu`
--

DROP TABLE IF EXISTS `t_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_role_menu` (
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID',
  `MENU_ID` bigint(20) NOT NULL COMMENT '菜单/按钮ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_role_menu`
--

LOCK TABLES `t_role_menu` WRITE;
/*!40000 ALTER TABLE `t_role_menu` DISABLE KEYS */;
INSERT INTO `t_role_menu` VALUES (1,1),(1,3),(1,11),(1,12),(1,13),(1,160),(1,161),(1,4),(1,14),(1,15),(1,16),(1,162),(1,5),(1,17),(1,18),(1,19),(1,163),(1,6),(1,20),(1,21),(1,22),(1,164),(1,2),(1,8),(1,23),(1,10),(1,24),(1,170),(1,136),(1,171),(1,172),(1,113),(1,114),(1,127),(1,128),(1,129),(1,130),(1,131),(1,137),(1,138),(1,165),(1,139),(1,166),(1,177),(1,179),(1,180),(1,181),(1,182),(1,192),(1,193),(1,194),(1,184),(1,185),(1,186),(1,187),(1,196),(1,197),(1,198),(1,115),(1,132),(1,133),(1,135),(1,134),(1,126),(1,159),(1,116),(1,117),(1,119),(1,120),(1,121),(1,122),(1,123),(1,118),(1,125),(1,167),(1,168),(1,169);
/*!40000 ALTER TABLE `t_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERNAME` varchar(50) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(128) NOT NULL COMMENT '密码',
  `DEPT_ID` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `EMAIL` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `MOBILE` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `STATUS` char(1) NOT NULL COMMENT '状态 0锁定 1有效',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFY_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最近访问时间',
  `SSEX` char(1) DEFAULT NULL COMMENT '性别 0男 1女 2保密',
  `IS_TAB` char(1) DEFAULT NULL COMMENT '是否开启tab，0关闭 1开启',
  `THEME` varchar(10) DEFAULT NULL COMMENT '主题',
  `AVATAR` varchar(100) DEFAULT NULL COMMENT '头像',
  `DESCRIPTION` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (9,'admin','d04c341d1ecbe34ddd8c52efb93416b1',1,'','','1','2019-07-08 12:51:23','2019-07-16 17:18:29','2019-07-17 17:33:34','2','0','white','default.jpg','');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_role`
--

DROP TABLE IF EXISTS `t_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user_role` (
  `USER_ID` bigint(20) NOT NULL COMMENT '用户ID',
  `ROLE_ID` bigint(20) NOT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_role`
--

LOCK TABLES `t_user_role` WRITE;
/*!40000 ALTER TABLE `t_user_role` DISABLE KEYS */;
INSERT INTO `t_user_role` VALUES (9,1);
/*!40000 ALTER TABLE `t_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-07-17 17:57:54
SET global sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
SET session sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';