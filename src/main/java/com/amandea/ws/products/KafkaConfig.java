package com.amandea.ws.products;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

//i will use this class to create kafka related beans, that's why i will annotate this class with configuration annotation.
@Configuration
public class KafkaConfig {

    //i will create a new method that will be responsible for creating a new Kafka topic.
    //this method will create a new kafka topic and return it as an object from this method.


    @Bean
    NewTopic createTopic(){
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas","2"))
                .build();
    }

}
