#!/bin/bash

# 进入当前文件目录
cd `dirname $0`
# 项目路径
DEPLOY_DIR=`pwd`

sh stop.sh

sh start.sh