#!/usr/bin/env bash

# restart for gateway application

if [ -z $JAVA_HOME ]; then
  echo "ERROR: JAVA_HOME is not found in your Environment."
  exit 1
fi
echo "Using JAVA_HOME: $JAVA_HOME"

# the path of the application installed.
APP_HOME=$(cd "`dirname $0`/.."; pwd)
echo "Using APP_HOME: $APP_HOME"

num=`ps -ef|grep "org.springframework.boot.loader.JarLauncher"|grep $APP_HOME |grep -v grep | awk '{print $2}'`
if [ -z $num ]; then
   $APP_HOME/bin/startup.sh $1
else
   kill -9 $num
   $APP_HOME/bin/startup.sh $1
fi