package com.arism.controller;

import com.arism.dto.OrderDto;
import com.arism.model.Order;
import com.arism.model.User;
import com.arism.repository.OrderRepository;
import com.arism.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDto> save(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestParam String address,
                                         @RequestParam String phoneNumber) {
        Long userId = ((User) userDetails).getId();
        OrderDto orderDto = orderService.createOrder(userId, address, phoneNumber);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orderDtos = orderService.getAllOrders();
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = ((User) userDetails).getId();
        List<OrderDto> orderDtos = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<OrderDto> getOrdersByUser(@PathVariable Long orderId,
                                                    @RequestParam Order.OrderStatus status) {
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
