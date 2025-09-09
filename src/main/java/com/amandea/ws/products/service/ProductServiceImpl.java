package com.amandea.ws.products.service;

import com.amandea.ws.products.rest.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

//it will allow spring framework to discover this class at the time when our application starts up, create a new instance of this
//class and add it to spring application context. and once the object of this class is in spring application context, we can use spring
//dependency injection.
@Service
public class ProductServiceImpl implements ProductService{

    private final  Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    //this class comes from spring framework, and it was designed to send messages or to publish events to kafka topic.
    //this class is a small wrapper around kafka producer , and it is nicely integrated with spring features like dependency injection and automatic configuration.

    //key data type and value object type defined in this <>
    KafkaTemplate<String,ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) throws Exception {
        String productId = UUID.randomUUID().toString();
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, product.getTitle(),product.getPrice(),product.getQuantity());

        /*
        //this will send a message to kafka topic asynchronously, and it will not wait for acknowledgement, that the message was successfully persisted.
        //if i send this message asynchronously and it fails to be persisted in kafka topic, the client application will not know about it and they will receive product ID.
        kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent);
        */
/*
        //but if you want to send message asynchronously and still be notified when it completes, then you can do it below way.

        //this Completablefuture class is a class in java concurrency Api, and it represents a future result of asynchronous computation. it is used to perform operation
        //asynchronously and then return result of that operation when it gets completed, and to handle the result when operation completes, we can use when complete method
        CompletableFuture<SendResult<String,ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent);

        //it accepts lambda and it will give you a result object and exception object. it is used to handle the result regardless whether it is successful or not successful method

        future.whenComplete((result,exception)->{
            if(exception != null){
                LOGGER.error("****************** Failed to send message: " + exception.getMessage());
            }
            else {
                //result.getRecordMetadata() is used to retrieve metadata associated with successfully sent message to kafka topic.
                LOGGER.info("*********** Message sent successfully: " + result.getRecordMetadata());
            }

        });
        LOGGER.info("******** Returning product id");
         //for synchronous call
        //this method will block the current thread until the future is complete ,and returns the result of computation, when it is available.
//        future.join();
       */

        //it's a way to make synchronous call
        //it will wait for acknowledgement from all kafka brokers that the message is successfully stored in kafka topic.
        //to make this send method , wait until it receives acknowledgement from kafka broker, i need to call get method on it, this method throws 2 checked exception.
        LOGGER.info("************** Before publishing ProductCreatedEvent");
        SendResult<String,ProductCreatedEvent> result = kafkaTemplate.send("product-created-events-topic",productId,productCreatedEvent).get();
        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("Offset: " + result.getRecordMetadata().offset());

        LOGGER.info("******** Returning product id");
        return productId;
    }
}
