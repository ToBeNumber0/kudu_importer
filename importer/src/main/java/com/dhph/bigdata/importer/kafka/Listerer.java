package com.dhph.bigdata.importer.kafka;

import com.dhph.bigdata.importer.kudu.KuduAgent;
import com.dhph.bigdata.importer.kudu.KuduAgentUtils;
import com.dhph.bigdata.importer.kudu.KuduRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kudu.Type;
import org.apache.kudu.client.KuduClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wyq
 * @create 2019/4/17
 * @Description
 **/
@Slf4j
@Component
public class Listerer {


    @Autowired
    private KuduAgent agent;

    @KafkaListener(topicPattern = "${spring.kafka.template.default-topic}")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
//            log.info("----------------- record =topic："  + topic+ ", " + record);
            log.debug("------------------ message =topic：" + topic+ ", " + message);
            consumer(message, topic);
        }
    }

    private void consumer(Object message,String topic){
        try {
            KuduRow rows = agent.transfer(message.toString());
            agent.insert(rows);
        }catch (Exception e){
            log.error("插入异常,topic{},msg:{}",topic, message.toString(), e);
        }
    }
}
