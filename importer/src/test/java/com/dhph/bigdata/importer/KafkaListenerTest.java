package com.dhph.bigdata.importer;

import com.dhph.bigdata.importer.kafka.Listerer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Wyq
 * @create 2019/4/17
 * @Description
 **/
@Slf4j
public class KafkaListenerTest {

    @Resource
    Listerer listerer;

    @Test
    void testKafkaListener(){
    }
}
