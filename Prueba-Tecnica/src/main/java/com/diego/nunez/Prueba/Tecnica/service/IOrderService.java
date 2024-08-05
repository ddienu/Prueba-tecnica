package com.diego.nunez.Prueba.Tecnica.service;

import com.diego.nunez.Prueba.Tecnica.entity.Order;
import org.apache.coyote.BadRequestException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    Order createOrder(String email, List<Integer> productIds, List<Integer> quantity, Map<Integer, Integer> relationOrder) throws BadRequestException;
    List<Order> getOrdersByUser(Integer userId);
    List<Order> getAllOrders();
    Order updateOrderStatus(Integer orderId, String newStatus);
    void removeOrder(Integer orderId);
}
