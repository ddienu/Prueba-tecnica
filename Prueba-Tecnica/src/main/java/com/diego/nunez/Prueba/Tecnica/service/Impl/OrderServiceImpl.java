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
import com.diego.nunez.Prueba.Tecnica.service.IOrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(IOrderRepository orderRepository, IUserRepository userRepository, IProductRepository productRepository){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    @Override
    public Order createOrder(String email, List<Integer> productIds, Map<Integer, Integer> relationOrder){
        Users userFounded = userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadCredentialsException("User not found")
        );
        List<Product> productList = productRepository.findAllById(productIds);
        if( productList.isEmpty()){
            throw new EmptyProductListException("Please add a product to your list");
        }
        Integer sumQuantity = 0;
        double prices = 0;

        for(Integer quantity : relationOrder.values()){
            sumQuantity+=quantity;
        }

        for(Map.Entry<Integer, Integer> entry : relationOrder.entrySet()){
            Integer productId = entry.getKey();
            Product productFounded = productRepository.findById(productId).get();
            Integer quantity = entry.getValue();
            if( quantity > productFounded.getStock()){
                throw new UnprocessableOrderException("The order exceeds the product stock");
            }
            prices += productFounded.getPrice() * quantity;
        }
        Order orderCreated = Order.builder()
                .user(userFounded)
                .creationDate(LocalDateTime.now())
                .products(productList)
                .quantity(sumQuantity)
                .status("PENDING")
                .total(prices)
                .build();

        return orderRepository.save(orderCreated);
    }

    @Override
    public List<Order> getOrdersByUser(Integer userId) {
        List<Order> ordersFounded = orderRepository.findOrdersByUserId(userId);
        if(ordersFounded.isEmpty()){
            throw new NoOrdersFoundedException("No orders founded for this user");
        }
        return ordersFounded;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrdersFoundedException("No order founded with id: " + orderId)
        );
        order.setStatus(newStatus.toUpperCase());
        return orderRepository.save(order);
    }

    @Override
    public void removeOrder(Integer orderId) {
        Order orderFounded = orderRepository.findById(orderId).orElseThrow(
                () -> new NoOrdersFoundedException("No order founded with id: " + orderId)
        );
        orderRepository.delete(orderFounded);
    }
}
