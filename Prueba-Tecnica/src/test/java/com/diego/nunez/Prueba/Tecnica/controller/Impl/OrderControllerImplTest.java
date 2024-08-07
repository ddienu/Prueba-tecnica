package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.OrderRequest;
import com.diego.nunez.Prueba.Tecnica.dto.ProductRequest;
import com.diego.nunez.Prueba.Tecnica.entity.Order;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.exception.EmptyProductListException;
import com.diego.nunez.Prueba.Tecnica.exception.NoOrdersFoundedException;
import com.diego.nunez.Prueba.Tecnica.exception.UnprocessableOrderException;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.OrderServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.transform.Result;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;

@WebMvcTest(controllers = OrderControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    @MockBean
    private JwtServiceImpl jwtService;

    @MockBean
    private IUserRepository userRepository;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private Order order;

    @MockBean
    private Users user;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        Product product = new Product();
        product.setId(1);
        product.setCategory("Food");
        product.setStock(3);
        product.setPrice(35.0);
        product.setDescription("Food");
        product.setName("Jam");

        List<Product> productList = new ArrayList<>();
        productList.add(product);

        user = new Users();
        user.setId(1);
        user.setRole("USER");
        user.setEmail("test@test.com");
        user.setPassword("12345");
        user.setName("Alvaro");

        order.setId(1);
        order.setProducts(productList);
        order.setStatus("PENDING");
        order.setQuantity(2);
        order.setTotal(350.0);
        order.setCreationDate( LocalDateTime.now() );
        order.setUser(user);
    }

    @Test
    void createOrderSuccess() throws Exception{
        Map<Integer, Integer> relationOrder = new HashMap<>();
        relationOrder.put(1, 2);

        given(orderService.createOrder(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willReturn(order);

        ResultActions response = mockMvc.perform(post("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Order created successfully")));
    }

    @Test
    void createOrderThrowsBadCredentialsException() throws Exception{

        given(jwtService.getTokenFromRequest(ArgumentMatchers.any(HttpServletRequest.class))).willReturn("testToken");
        given(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).willReturn("test@test.com");

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1);
        productRequest.setQuantity(1);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(Arrays.asList(productRequest));

        given(orderService.createOrder(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willThrow(new BadCredentialsException("User not found"));

        ResultActions response = mockMvc.perform(post("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("User not found")));
    }

    @Test
    void createOrderThrowsEmptyProductListException() throws Exception{
        given(jwtService.getTokenFromRequest(ArgumentMatchers.any(HttpServletRequest.class))).willReturn("testToken");
        given(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).willReturn("test@test.com");

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1);
        productRequest.setQuantity(1);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(Arrays.asList(productRequest));

        given(orderService.createOrder(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willThrow(new EmptyProductListException("Please add a product to your list"));

        ResultActions response = mockMvc.perform(post("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));


        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Please add a product to your list")));
    }

    @Test
    void createOrderThrowsUnprocessableOrderException() throws Exception{
        given(jwtService.getTokenFromRequest(ArgumentMatchers.any(HttpServletRequest.class))).willReturn("testToken");
        given(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).willReturn("test@test.com");

        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(1);
        productRequest.setQuantity(1);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProducts(Arrays.asList(productRequest));

        given(orderService.createOrder(ArgumentMatchers.anyString(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .willThrow(new UnprocessableOrderException("The order exceeds the product stock"));

        ResultActions response = mockMvc.perform(post("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));


        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("The order exceeds the product stock")));

    }

    @Test
    void getOrdersByIdSuccess() throws Exception{
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        given(jwtService.getTokenFromRequest(any(HttpServletRequest.class))).willReturn("testToken");
        given(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).willReturn("test@test.com");
        given(userRepository.getUserByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(user));

        given(orderService.getOrdersByUser(ArgumentMatchers.any())).willReturn(orderList);

        ResultActions response = mockMvc.perform(get("/v1/order/userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Orders by UserId " + user.getId() + " founded: ")));
    }

    @Test
    void getOrdersByIdThrowsNoOrdersFoundedException() throws Exception{

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        given(jwtService.getTokenFromRequest(any(HttpServletRequest.class))).willReturn("testToken");
        given(jwtService.getEmailFromToken(ArgumentMatchers.anyString())).willReturn("test@test.com");
        given(userRepository.getUserByEmail(ArgumentMatchers.anyString())).willReturn(Optional.of(user));
        given(orderService.getOrdersByUser(ArgumentMatchers.anyInt())).willThrow(new NoOrdersFoundedException("No orders founded for this user"));


        ResultActions response = mockMvc.perform(get("/v1/order/userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("No orders founded for this user")));
    }

    @Test
    void getAllOrdersSuccess() throws Exception{
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        given(orderService.getAllOrders()).willReturn(orderList);

        ResultActions response = mockMvc.perform(get("/v1/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Those orders were founded")));
    }

    @Test
    void updateOrderStatusSuccess() throws Exception{
        given(orderService.updateOrderStatus(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
                .willReturn(order);

        ResultActions response = mockMvc.perform(put("/v1/order/{orderId}/{newStatus}", order.getId(), "PROCESSING")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Status updated successfully")));
    }

    @Test
    void removeOrderSuccess() throws Exception{
        willDoNothing().given(orderService).removeOrder(ArgumentMatchers.anyInt());

        ResultActions response = mockMvc.perform(delete("/v1/order/{orderId}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.Data.message", CoreMatchers.is("Order removed successfully")));
    }


}
