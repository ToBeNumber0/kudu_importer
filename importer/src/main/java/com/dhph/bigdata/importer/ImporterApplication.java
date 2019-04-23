package com.dhph.bigdata.importer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//@EnableKafka
@Slf4j
public class ImporterApplication {


    public static void main(String[] args) {
		SpringApplication.run(ImporterApplication.class, args);
        log.info("Application startup succeed!");
    }
}

