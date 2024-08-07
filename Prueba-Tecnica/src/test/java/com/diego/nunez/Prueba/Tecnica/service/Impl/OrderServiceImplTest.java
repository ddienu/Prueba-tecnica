package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Order;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.exception.EmptyProductListException;
import com.diego.nunez.Prueba.Tecnica.exception.NoOrdersFoundedException;
import com.diego.nunez.Prueba.Tecnica.exception.UnprocessableOrderException;
import com.diego.nunez.Prueba.Tecnica.repository.IOrderRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrderThrowsBadCredentialsException(){
        String email = "asd";
        List<Integer> productIds = new ArrayList<>();
        Map<Integer, Integer> relationOrder = new HashMap<>();

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            orderService.createOrder(email, productIds, relationOrder);
        });
    }

    @Test
    void createOrderThrowsEmptyProductionListException(){
        String email = "test@test.com";
        List<Integer> productIds = new ArrayList<>();
        Map<Integer, Integer> relationOrder = new HashMap<>();
        Users user = new Users();

        user.setEmail(email);
        productIds.add(1);
        relationOrder.put(1, 1);

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(productIds)).thenReturn(new ArrayList<>());


        Assertions.assertThrows(EmptyProductListException.class, () -> {
            orderService.createOrder(email, productIds, relationOrder);
        });
    }

    @Test
    void createOrderThrowsUnprocessableOrderException(){
        String email = "test@test.com";
        List<Integer> productIds = new ArrayList<>();
        Map<Integer, Integer> relationOrder = new HashMap<>();
        Users user = new Users();
        List<Product> productList = new ArrayList<>();

        user.setEmail(email);
        productIds.add(1);
        relationOrder.put(1, 4);

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        productList.add(product);

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(productIds)).thenReturn(productList);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Assertions.assertThrows(UnprocessableOrderException.class, () -> {
            orderService.createOrder(email, productIds, relationOrder);
        });
    }

    @Test
    void createOrderSuccessfully(){
        String email = "test@test.com";
        List<Integer> productIds = new ArrayList<>();
        Map<Integer, Integer> relationOrder = new HashMap<>();
        Users user = new Users();
        List<Product> productList = new ArrayList<>();

        user.setEmail(email);
        productIds.add(1);
        relationOrder.put(1, 1);

        Product product = new Product();
        product.setId(1);
        product.setPrice(35.0);
        product.setDescription("Description");
        product.setStock(3);
        product.setCategory("Food");
        product.setName("Jam");

        productList.add(product);

        Order orderCreated = Order.builder()
                .user(user)
                .creationDate(LocalDateTime.now())
                .products(productList)
                .quantity(1)
                .status("PENDING")
                .total(product.getPrice())
                .build();

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(productIds)).thenReturn(productList);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(orderCreated);

        Order result = orderService.createOrder(email, productIds, relationOrder);

        Assertions.assertEquals(orderCreated.getUser().getEmail(), result.getUser().getEmail());
    }

    @Test
    void getOrdersByUserOK(){
        Integer userId = 1;
        List<Order> orderList = new ArrayList<>();
        Users user = new Users();
        user.setEmail("test@test.com");
        Order order = Order.builder()
                        .user(user)
                        .creationDate(LocalDateTime.now())
                        .products(new ArrayList<>())
                        .quantity(1)
                        .status("PENDING")
                        .total(25.0)
                        .build();
        orderList.add(order);

        when(orderRepository.findOrdersByUserId(userId)).thenReturn(orderList);

        List<Order> result = orderService.getOrdersByUser(userId);

        Assertions.assertEquals(orderList, result);
        verify(orderRepository, times(1)).findOrdersByUserId(userId);
    }

    @Test
    void getOrdersByUserThrowsNoOrdersFoundedException(){
        Integer userId = 1;

        when(orderRepository.findOrdersByUserId(userId)).thenReturn(new ArrayList<>());

        Assertions.assertThrows(NoOrdersFoundedException.class, () -> {
            orderService.getOrdersByUser(userId);
        });
    }

    @Test
    void getAllOrdersOK(){
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        orderService.getAllOrders();

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void updateOrderStatusOK(){
        Integer orderId = 1;
        String newStatus = "PROCESSING";

        Order order = new Order();
        order.setStatus("PENDING");
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order result = orderService.updateOrderStatus(orderId, newStatus );

        Assertions.assertEquals(newStatus.toUpperCase(), result.getStatus());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void updateOrderStatusThrowsException(){
        Integer orderId = 1;
        String newStatus = "PROCESSING";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoOrdersFoundedException.class, () -> {
            orderService.updateOrderStatus(orderId, newStatus);
        });
    }

    @Test
    void removeOrderOK(){
        Integer orderId = 1;

        Order order = new Order();
        order.setId(1);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.removeOrder(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void removeOrderThrowsException(){
        Integer orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoOrdersFoundedException.class, () -> {
            orderService.removeOrder(orderId);
        });
    }
}
