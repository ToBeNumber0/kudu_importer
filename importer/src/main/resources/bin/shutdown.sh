#!/usr/bin/env bash

# shutdown for gateway application

if [ -z $JAVA_HOME ]; then
  echo "ERROR: JAVA_HOME is not found in your Environment."
  exit 1
fi
echo "Using JAVA_HOME: $JAVA_HOME"

# the path of the application installed.
APP_HOME=$(cd "`dirname $0`/.."; pwd)
echo "Using APP_HOME: $APP_HOME"

num=`ps -ef|grep "org.springframework.boot.loader.JarLauncher" |grep -v grep | awk '{print $2}'`
kill -9 $num
