package com.ktube.uploading.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-server")
    private String kafkaBootstrapServerIpAddress;


    @Bean
    public ProducerFactory<String, String> producerFactory() {

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServerIpAddress);
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }
}
