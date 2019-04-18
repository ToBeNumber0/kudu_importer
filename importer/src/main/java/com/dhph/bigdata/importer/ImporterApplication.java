package com.dhph.bigdata.importer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableKafka
//@ComponentScan(basePackages = {"com.dhph.bigdata.importer"})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Slf4j
public class ImporterApplication {

    public static void main(String[] args) {
		SpringApplication.run(ImporterApplication.class, args);
        log.info("Application startup succeed!");
//        ApplicationContext appctx = SpringApplication.run(ImporterApplication.class, args);
//        System.out.println("appctx.getBeanDefinitionCount=" + appctx.getBeanDefinitionCount());
//        try {
//            ((ConfigurableApplicationContext) appctx).close();
//        } catch (Exception e) { /*ignore*/ }
    }
}

