package com.dhph.bigdata.importer.kafka;

import com.dhph.bigdata.common.utils.CommonUtil;
import com.dhph.bigdata.importer.kudu.KuduAgent;
import com.dhph.bigdata.importer.kudu.KuduRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
public class Listener {


    @Autowired
    private KuduAgent agent;

    @KafkaListener(topicPattern = "${spring.kafka.template.default-topic}")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        log.info("----------------- record =topic："  + topic+ ", " + record);
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        kafkaMessage.ifPresent(u->consumer(u, topic));
    }

    private void consumer(Object message,String topic){
        log.debug("------------------ message =topic：" + topic+ ", " + message);
        try {
            KuduRow row = agent.transfer(message.toString());
            if(CommonUtil.isObjectNotEmpty(row)){
                agent.insert(row);
            }else{
                log.warn("数据转换异常，已跳过");
            }
        }catch (Exception e){
            log.error("插入异常,topic{},msg:{}",topic, message.toString(), e);
        }
    }
}
