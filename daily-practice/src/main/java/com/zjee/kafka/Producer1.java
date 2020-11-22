package com.zjee.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Stack;

/**
 * @author zhongjie
 * @Date 2020/6/28
 * @E-mail zjee@live.cn
 * @Desc
 */

@Slf4j
public class Producer1 {

    public static void main(String[] args){
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.CLIENT_ID_CONFIG, "producer-01");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>("my-topic-01", "hello kafka");
            producer.send(record, (recordMetadata, e) -> {
                if (e != null) {
                    log.error("发送消息失败: ", e);
                } else {
                    log.info(recordMetadata.topic() + "-p:"+recordMetadata.partition() +"-o:" + recordMetadata.offset());
                }
            });
        } catch (Exception e) {
            log.error("发送消息失败: ", e);
        }

        new Stack().clear();
    }
}
