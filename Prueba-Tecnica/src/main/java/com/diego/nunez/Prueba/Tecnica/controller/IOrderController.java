package com.diego.nunez.Prueba.Tecnica.controller;

import com.diego.nunez.Prueba.Tecnica.dto.OrderRequest;
import com.diego.nunez.Prueba.Tecnica.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrderController {

    ResponseEntity<Response> createOrder(OrderRequest order, HttpServletRequest request) throws BadRequestException;
    ResponseEntity<Response> getOrdersById(HttpServletRequest request);
    ResponseEntity<Response> getAllOrders();
    ResponseEntity<Response> updateOrderStatus(Integer orderId, String newStatus);
    ResponseEntity<Response> removeOrder(Integer orderId);
}
