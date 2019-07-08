#项目说明

## 简介

* 本项目可从kafka中读取约定好格式的数据并写入kudu大数据平台。

* 仅可支持单个表的数据导入，若需导入多个表，可修改配置后启动多个实例。

* 需要提前在kudu中建好表，数据结构参考对应kafka写入的数据

## 如何运行

1. 配置kafka。  在resources/application.yml 修改kafka的主机地址`kafka.bootstrap-servers`，
和写入的topic`kafka.template.default-topic`。
2. 配置kudu。在resources/application.yml 修改kudu的主机地址`kudu.master`，
          和对应表`kudu.table`。
3. 运行com.dhph.bigdata.importer.ImporterApplication。若在服务器上运行，可打成jar包， 直接运行即可。 
       