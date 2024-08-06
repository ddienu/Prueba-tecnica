package com.diego.nunez.Prueba.Tecnica.dto;

import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderRequestTest {

    @Test
    void productListOk(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1);
        productRequest.setQuantity(3);
        OrderRequest orderRequest = new OrderRequest();
        List<ProductRequest> productList = new ArrayList<>();
        productList.add(productRequest);
        orderRequest.setProducts(productList);
        Assert.notEmpty(orderRequest.getProducts());
    }
}
