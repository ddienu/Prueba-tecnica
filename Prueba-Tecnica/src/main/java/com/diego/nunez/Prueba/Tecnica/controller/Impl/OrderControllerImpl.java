package com.diego.nunez.Prueba.Tecnica.controller.Impl;

import com.diego.nunez.Prueba.Tecnica.controller.IOrderController;
import com.diego.nunez.Prueba.Tecnica.dto.OrderRequest;
import com.diego.nunez.Prueba.Tecnica.dto.ProductRequest;
import com.diego.nunez.Prueba.Tecnica.dto.Response;
import com.diego.nunez.Prueba.Tecnica.dto.ResponseDataDto;
import com.diego.nunez.Prueba.Tecnica.entity.Order;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.Impl.JwtServiceImpl;
import com.diego.nunez.Prueba.Tecnica.service.Impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/v1/order")
public class OrderControllerImpl implements IOrderController {
    private final OrderServiceImpl orderService;
    private final JwtServiceImpl jwtService;
    private final IUserRepository userRepository;

    @Autowired
    public OrderControllerImpl(OrderServiceImpl orderService, JwtServiceImpl jwtService, IUserRepository userRepository){
        this.orderService = orderService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> createOrder(@RequestBody OrderRequest order, HttpServletRequest request) throws BadRequestException {
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        List<Integer> productList = new ArrayList<>();
        Map<Integer, Integer> relationOrder = new HashMap<>();
        for(ProductRequest productRequest : order.getProducts()){
            Integer productId = productRequest.getProductId();
            Integer quantity = productRequest.getQuantity();
            productList.add(productId);
            relationOrder.put(productId, quantity);
        }
        orderService.createOrder(email, productList, relationOrder);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Order created successfully")
                                .build()
                ), HttpStatus.OK
        );
    }

    @GetMapping(path = "/userId", produces = "application/json")
    @Override
    public ResponseEntity<Response> getOrdersById(HttpServletRequest request) {
        String token = jwtService.getTokenFromRequest(request);
        String email = jwtService.getEmailFromToken(token);
        Users user = userRepository.getUserByEmail(email).get();
        List<Order> orderList = orderService.getOrdersByUser(user.getId());
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Orders by UserId " + user.getId() + " founded: ")
                                .orders(orderList)
                                .build()
                ), HttpStatus.OK
        );
    }

    @GetMapping(produces = "application/json")
    @Override
    public ResponseEntity<Response> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Those orders were founded")
                                .orders(orderList)
                                .build()
                ), HttpStatus.OK
        );
    }

    @PutMapping(path = "/{orderId}/{newStatus}")
    @Override
    public ResponseEntity<Response> updateOrderStatus(@PathVariable Integer orderId, @PathVariable String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Status updated successfully")
                                .build()
                ), HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/{orderId}", produces = "application/json")
    @Override
    public ResponseEntity<Response> removeOrder(@PathVariable Integer orderId) {
        orderService.removeOrder(orderId);
        return new ResponseEntity<>(
                new Response(
                        ResponseDataDto.builder()
                                .message("Order removed successfully")
                                .build()
                ), HttpStatus.OK
        );
    }
}
