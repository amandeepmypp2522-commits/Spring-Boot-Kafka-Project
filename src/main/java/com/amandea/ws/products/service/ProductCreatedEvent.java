package com.amandea.ws.products.service;

import java.math.BigDecimal;

public class ProductCreatedEvent {

    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    //no- args constructor here it is needed for deserialization purpose because when kafka producer publishes this event object,
    //it will be serialized into a byte array.consumer microservice will need to deserialize this message using this same product
    //created event class and the deserialization process,it typically requires no args constructor to create instance of this class
    //before it can populate member variables with data.

    //deserialization process involves creating empty instances of this class using no args constructor, and then populating these
    //fields using these setter methods.
    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String title, String productId, BigDecimal price, Integer quantity) {
        this.title = title;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
