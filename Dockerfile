FROM reg.dh-home.cn/base/java:8

ENV appname dh-bigdata-importer
ENV apppath /data/apps/
ENV appjar dh-bigdata-importer-19.2.14-SNAPSHOT.jar

COPY ./importer/target/$appjar $apppath

ENV server_port 18151
ENV server_context_path /bigdata
ENV spring_profiles_active test

ENV spring_redis_database 0
ENV spring_redis_host 172.16.10.110
ENV spring_redis_port 30381
ENV spring_redis_password Dhph2019

ENV spring_datasource_bjhj_url jdbc:mysql://172.16.10.59:3306/bigdata?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
ENV spring_datasource_bjhj_username bigdata
ENV spring_datasource_bjhj_password Bjhj_123456

ENV spring_datasource_wh_url jdbc:mysql://172.16.10.59:3306/winghead?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
ENV spring_datasource_wh_username winghead
ENV spring_datasource_wh_password winghead_test

ENV bihj_user dehongjr
ENV bihj_pwd dehongjr@123
ENV bihj_sourceCode BJ20171219002
ENV bihj_address https://test.bjp2p.com.cn:8443/platformService
ENV bihj_nameSpace http://supervise.service.app.mp.zkbc.net/

ENV xxl_job_admin_addresses http://test-job.dh-home.cn/
ENV xxl_job_executor_appname bigdata
ENV xxl_job_executor_port 8151
ENV xxl_job_executor_logpath /tmp/bigdata/logs


ENV bihj_submitthreadpool_coreandmaxpoolsize 8
ENV bihj_submitthreadpool_threadsleepmillis 60000
ENV bihj_submitthreadpool_querybatchlimit 1000
ENV bihj_submitthreadpool_queueaccept 3000
ENV bihj_submitthreadpool_queuemaxsize 10000

ENV huxie_uploadUrl http://huxie.dh-ali.cn/analyze/uploadApi
ENV huxie_saveCsvPath /data/apps/csv

WORKDIR $apppath

RUN mkdir $huxie_saveCsvPath

CMD java -jar $appjar --spring.profiles.active=$spring_profiles_active \
 --spring.redis.database=$spring_redis_database \
 --spring.redis.host=$spring_redis_host \
 --spring.redis.port=$spring_redis_port \
 --spring.redis.password=$spring_redis_password \
 --spring.datasource.bigdata.url=$spring_datasource_bjhj_url \
 --spring.datasource.bigdata.username=$spring_datasource_bjhj_username \
 --spring.datasource.bigdata.password=$spring_datasource_bjhj_password \
 --spring.datasource.wh.url=$spring_datasource_wh_url \
 --spring.datasource.wh.username=$spring_datasource_wh_username \
 --spring.datasource.wh.password=$spring_datasource_wh_password \
 --server.port=$server_port \
 --server.context-path=$server_context_path \
 --bihj.user=$bihj_user \
 --bihj.pwd=$bihj_pwd \
 --bihj.sourceCode=$bihj_sourceCode \
 --bihj.address=$bihj_address \
 --bihj.nameSpace=$bihj_nameSpace \
 --xxl.job.admin.addresses=$xxl_job_admin_addresses \
 --xxl.job.executor.appname=$xxl_job_executor_appname \
 --xxl.job.executor.port=$xxl_job_executor_port \
 --xxl.job.executor.logpath=$xxl_job_executor_logpath \
 --huxie.uploadUrl=$huxie_uploadUrl \
 --huxie.saveCsvPath=$huxie_saveCsvPath \
 --bihj.submitThreadPool.coreAndMaxPoolSize=$bihj_submitthreadpool_coreandmaxpoolsize \
 --bihj.submitThreadPool.threadSleepMillis=$bihj_submitthreadpool_threadsleepmillis \
 --bihj.submitThreadPool.queryBatchLimit=$bihj_submitthreadpool_querybatchlimit \
 --bihj.submitThreadPool.queueAccept=$bihj_submitthreadpool_queueaccept \
 --bihj.submitThreadPool.queueMaxSize=$bihj_submitthreadpool_queuemaxsize \



