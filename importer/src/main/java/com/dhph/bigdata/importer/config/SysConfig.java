package com.dhph.bigdata.importer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Wyq
 * @create 2019/4/23
 * @Description
 **/
@Component
public class SysConfig {

    @Value("${kudu.master}")
    public String master ;

    @Value("${kudu.table}")
    public String tableName ;



}
