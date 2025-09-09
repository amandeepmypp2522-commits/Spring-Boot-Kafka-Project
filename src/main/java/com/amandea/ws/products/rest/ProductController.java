package com.amandea.ws.products.rest;


import com.amandea.ws.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;
    //you can turn logger off using application properties file of this microservice.
    private final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    //at the time when our application starts up, spring framework will scan all packages of our application looking for classes with
    //annotations like Rest Controller and service, it will then create new instances of productServiceImpl object and it will inject
    //it into our rest controller.

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProducts(@RequestBody CreateProductRestModel product){
        String productId;
        try{
             productId = productService.createProduct(product);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(new ErrorMessage(LocalDate.now(),e.getMessage(),"/products"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

}
