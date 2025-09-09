package com.amandea.ws.products.service;

import com.amandea.ws.products.rest.CreateProductRestModel;

public interface ProductService {

    String createProduct(CreateProductRestModel product) throws Exception;
}
