#!/bin/bash

# java 其他配置参数
JAVA_OPTS=""
# 进入当前文件目录
cd `dirname $0`
cd ../
# 项目路径
DEPLOY_DIR=`pwd`

# 项目配置文件
CONFIG_FILE=$DEPLOY_DIR/config/config.json
# 日志配置文件
LOG_FILE="-Dlogback.configurationFile=$DEPLOY_DIR/config/logback.xml"

# lib
LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

# 判断是否已经启动
PID=`ps -ef | grep "$DEPLOY_DIR" | grep -v 'grep' | awk '{print $2}'`

if [ -n "$PID" ]; then
    echo "已经启动"
else
    # 启动命令
    java $JAVA_OPTS $LOG_FILE -classpath $LIB_JARS com.hebaibai.plumber.Main -c $CONFIG_FILE > sout.log 2>&1 &
    echo "启动成功"
fi
