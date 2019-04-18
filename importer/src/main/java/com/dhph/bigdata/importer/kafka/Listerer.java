package com.dhph.bigdata.importer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Wyq
 * @create 2019/4/17
 * @Description
 **/
@Slf4j
@Component
public class Listerer {

    @KafkaListener(topicPattern = "pd_product")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("----------------- record =topic："  + topic+ ", " + record);
            log.info("------------------ message =topic：" + topic+ ", " + message);
        }
    }
}
