package com.dhph.bigdata.importer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableKafka
@Slf4j
public class ImporterApplication {


    public static void main(String[] args) {
//		SpringApplication.run(ImporterApplication.class, args);
        new SpringApplicationBuilder().sources(ImporterApplication.class).web(false).run(args);
        log.info("Application startup succeed!");
    }
}

