package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.controller.auth.AuthController;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.exception.ProductNotFoundException;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.ProductServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(controllers = ProductControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private Product product;

    @BeforeEach
    void setUp(){
        product = new Product();
        product.setId(1);
        product.setCategory("Food");
        product.setStock(3);
        product.setPrice(35.0);
        product.setDescription("Food");
        product.setName("Jam");
    }

    @Test
    void getAllProductsOK() throws Exception {
        List<Product> productsFounded = new ArrayList<>();
        productsFounded.add(product);

        given(productService.getAllProducts()).willReturn(productsFounded);

        ResultActions response = mockMvc.perform(get("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productsFounded)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Those products are founded")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].category", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].stock", CoreMatchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].price", CoreMatchers.is(35.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].description", CoreMatchers.is("Food")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.products[0].name", CoreMatchers.is("Jam")));
    }

    @Test
    void getProductByIdSuccess() throws Exception {
        given(productService.getProductById(ArgumentMatchers.any())).willReturn(Optional.of(product));

        ResultActions response = mockMvc.perform(get("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product with id " + product.getId() + " founded")));
    }

    @Test
    void getProductByIdThrowsProductNotFoundException() throws Exception {
        given(productService.getProductById(ArgumentMatchers.any())).willThrow(new ProductNotFoundException("Product not found"));

        ResultActions response = mockMvc.perform(get("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product not found")));
    }

    @Test
    void saveProductSuccess() throws Exception{
        given(productService.saveProduct(ArgumentMatchers.any())).willReturn(product);

        ResultActions response = mockMvc.perform(post("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product save successfully")));
    }

    @Test
    void putProductSuccess() throws Exception{
        given(productService.putProduct(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).willReturn(product);

        ResultActions response = mockMvc.perform(put("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product updated successfully")));
    }

    @Test
    void putProductThrowsProductNotFoundException() throws Exception{
        given(productService.putProduct(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).willThrow(new ProductNotFoundException("Product not found"));

        ResultActions response = mockMvc.perform(put("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product not found")));
    }

    @Test
    void putProductThrowsExpiredJwtException() throws Exception{
        String token = "testToken";

        given(productService.putProduct(ArgumentMatchers.anyInt(), ArgumentMatchers.any(Product.class)))
                .willThrow(new ExpiredJwtException(
                        null,
                        null,
                        "Token expired"
                ));

        ResultActions response = mockMvc.perform(put("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(product))
        );

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Token expired")));
    }

    @Test
    void putProductThrowsMalformedJwtException() throws Exception{
        String token = "testToken";

        given(productService.putProduct(ArgumentMatchers.anyInt(), ArgumentMatchers.any(Product.class)))
                .willThrow(new MalformedJwtException(
                        "Invalid token"
                ));

        ResultActions response = mockMvc.perform(put("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(product))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Invalid token")));
    }

    @Test
    void putProductThrowsSignatureException() throws Exception{
        String token = "testToken";

        given(productService.putProduct(ArgumentMatchers.anyInt(), ArgumentMatchers.any(Product.class)))
                .willThrow(new SignatureException(
                        "Invalid token"
                ));

        ResultActions response = mockMvc.perform(put("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(product))
        );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Invalid token")));
    }

    @Test
    void deleteProductSuccess() throws Exception{
        willDoNothing().given(productService).deleteProduct(ArgumentMatchers.anyInt());

        ResultActions response = mockMvc.perform(delete("/v1/product/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Product deleted successfully")));
    }

}
