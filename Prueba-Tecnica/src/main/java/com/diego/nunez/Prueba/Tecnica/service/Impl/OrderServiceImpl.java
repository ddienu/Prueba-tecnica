package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.entity.Order;
import com.diego.nunez.Prueba.Tecnica.entity.Product;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IOrderRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IProductRepository;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.IOrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    public Order createOrder(String email, List<Integer> productIds, List<Integer> quantities, Map<Integer, Integer> relationOrder) throws BadRequestException {
        Users userFounded = userRepository.getUserByEmail(email).orElseThrow(
                () -> new BadRequestException("User not found")
        );
        List<Product> productList = productRepository.findAllById(productIds);
        Integer sumQuantity = 0;
        double prices = 0;

        for( Integer quantity : quantities){
            sumQuantity += quantity;
        }

        for( int i = 0; i < productIds.size(); i++){
            Product product = productRepository.findById(productIds.get(i)).get();
            for(int j = 0; j < productIds.size(); j++){
                 prices+=product.getPrice()*quantities.get(i);
                break;
            }
        }
        if( productList.isEmpty()){
            throw new BadRequestException("Please add a product to your list");
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
        return orderRepository.findOrdersByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).get();
        order.setStatus(newStatus.toUpperCase());
        return orderRepository.save(order);
    }

    @Override
    public void removeOrder(Integer orderId) {
        Order orderFounded = orderRepository.findById(orderId).get();
        orderRepository.delete(orderFounded);
    }
}
