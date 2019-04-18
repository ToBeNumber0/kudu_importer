#!/usr/bin/env bash

#startup for mdm application
ACTIVE_PROFILE=$1
if [ -z $1 ]; then
    echo "active environment not specified use default: dev"
    ACTIVE_PROFILE="dev"
fi

if [ -z $JAVA_HOME ]; then
  echo "ERROR: JAVA_HOME is not found in your Environment."
  exit 1
fi
echo "Using JAVA_HOME: $JAVA_HOME"

# the path of the application installed.
APP_HOME=$(cd "`dirname $0`/.."; pwd)
echo "Using APP_HOME: $APP_HOME"

# add jar to classpath
CP=$CLASSPATH
for JAR in $APP_HOME/lib/*.jar
do
    CP=$CP:$JAR
done

echo "CLASSPATH: $CP"

#Dynamic Calculate Available Heap
maxFreeNeeded=10000

#-server//服务器模式
#-Xmx2g //JVM最大允许分配的堆内存，按需分配
#-Xms2g //JVM初始分配的堆内存，一般和Xmx配置成一样以避免每次gc后JVM重新分配内存。
#-Xmn256m //年轻代内存大小，整个JVM内存=年轻代 + 年老代 + 持久代
#-Xss256k //设置每个线程的堆栈大小
#-XX:+DisableExplicitGC //忽略手动调用GC, System.gc()的调用就会变成一个空调用，完全不触发GC
#-XX:+UseConcMarkSweepGC //并发标记清除（CMS）收集器
#-XX:+CMSParallelRemarkEnabled //降低标记停顿
#-XX:LargePageSizeInBytes=128m //内存页的大小
#-XX:+UseFastAccessorMethods //原始类型的快速优化
#-XX:+UseCMSInitiatingOccupancyOnly //使用手动定义初始化定义开始CMS收集
#-XX:CMSInitiatingOccupancyFraction=70 //使用cms作为垃圾回收使用70％后开始CMS收集

# Assume here that file system cache is also "free"
freeMem=`free -m | grep 'Mem' | awk '{print $7}'`
availMem=$freeMem

if [ $availMem -gt $maxFreeNeeded ]
then
    MAX_HEAP=4G
else
    MAX_HEAP=$(($availMem*8/10))
    MAX_HEAP=${MAX_HEAP}M
fi

JAVA_OPTS="-server -Xmx$MAX_HEAP -Xms$MAX_HEAP -Xmn256m -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8"

if [ $ACTIVE_PROFILE = "prod" ]; then
    nohup $JAVA_HOME/bin/java -DAPP_HOME=$APP_HOME -Dspring.profiles.active=$ACTIVE_PROFILE -cp $CP $JAVA_OPTS  -Xloggc:$APP_HOME/logs/gc.log org.springframework.boot.loader.JarLauncher --server.tomcat.basedir=$APP_HOME/logs --server.tomcat.access-log-enabled=true --server.tomcat.accesslog.directory=$APP_HOME/logs --server.tomcat.access-log-pattern='%{X-FORWARDED-FOR}i %l %u %t "%r" %s %b %D %q "%{User-Agent}i" %T' > /dev/null 2>&1 &
else
    nohup $JAVA_HOME/bin/java -DAPP_HOME=$APP_HOME -Dspring.profiles.active=$ACTIVE_PROFILE -cp $CP  -Xloggc:$APP_HOME/logs/gc.log org.springframework.boot.loader.JarLauncher --server.tomcat.basedir=$APP_HOME/logs --server.tomcat.access-log-enabled=true --server.tomcat.accesslog.directory=$APP_HOME/logs --server.tomcat.access-log-pattern='%{X-FORWARDED-FOR}i %l %u %t "%r" %s %b %D %q "%{User-Agent}i" %T' > /dev/null 2>&1 &
fi