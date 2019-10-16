#!/bin/bash

# 进入当前文件目录
cd `dirname $0`
cd ../
# 项目路径
DEPLOY_DIR=`pwd`

PID=`ps -ef | grep java | grep "$DEPLOY_DIR" | grep -v grep | awk '{print $2}'`

if [ -n "$PID" ]; then
    echo PID: $PID
    kill -9 $PID
fi
