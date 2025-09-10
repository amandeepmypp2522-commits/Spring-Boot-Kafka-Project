package com.amandea.ws.products;

import com.amandea.ws.products.service.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

//i will use this class to create kafka related beans, that's why i will annotate this class with configuration annotation.
@Configuration
public class KafkaConfig {

    //i will create a new method that will be responsible for creating a new Kafka topic.
    //this method will create a new kafka topic and return it as an object from this method.

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String requestTimeout;

    @Bean
    NewTopic createTopic(){
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas","2"))
                .build();
    }

    Map<String, Object> producerConfigs(){
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,keySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,valueSerializer);
        config.put(ProducerConfig.ACKS_CONFIG,acks);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,deliveryTimeout);
        config.put(ProducerConfig.LINGER_MS_CONFIG,linger);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,requestTimeout);
        return config;
    }

    //this method will be responsible for creating producer factory object that creates  kafka producer instances.
    //in spring framework Factory is a design pattern that is used to create objects, and in this case producer
    //factory is specifically designed to create kafka producer objects and kafka producer is responsible for sending messages to kafka topics.

    @Bean
    ProducerFactory<String, ProductCreatedEvent> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    //it will crate a bin of this type and will register it in spring application context.
    //we have already done but this time we are creating a new kafka template object that takes producer factory
    //bean as an argument, and this will create kafka template object that will use kafka producer with configuration properties that we
    //created in above method.

    //kafka template is a higher level object that wraps kafka producer, integrates it with spring framework and simplifies its usage,
    // and kafka producer is a low level API that actually sends message to kafka brokers.


    @Bean
    KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

}
