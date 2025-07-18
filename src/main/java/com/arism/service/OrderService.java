package com.arism.service;

import com.arism.dto.CartDto;
import com.arism.dto.OrderDto;
import com.arism.exception.InsufficientStockException;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.CartMapper;
import com.arism.mapper.OrderMapper;
import com.arism.model.*;
import com.arism.repository.OrderRepository;
import com.arism.repository.ProductRepository;
import com.arism.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    @Transactional
    public OrderDto createOrder(Long userId, String address, String phoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CartDto cartDto = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDto);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Can't create order when the cart is empty");
        }

        // create order object and fill the properties
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(Order.OrderStatus.PREPARING);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> orderItems = createOrderItems(cart, order);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        log.info("Order has been created with id: {}", savedOrder.getId());

        return orderMapper.toDto(savedOrder);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(
                cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + cartItem.getProduct().getName()));

                    if(product.getQuantity() == null){
                        throw new IllegalStateException("Product quantity is not set for product "+product.getName());
                    }
                    if(product.getQuantity() < cartItem.getQuantity()){
                        try {
                            throw new InsufficientStockException("Not enough stock for product "+product.getName());
                        } catch (InsufficientStockException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    // Substract product/inventory qty with order quantity when order placed/saved
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);

                    return new OrderItem(null, order, product, cartItem.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    public List<OrderDto> getAllOrders() {
        return orderMapper.toDtos(orderRepository.findAll());
    }

    public List<OrderDto> getAllOrdersByUserId(Long userId) {
        return orderMapper.toDtos(orderRepository.findByUserId(userId));
    }

    public OrderDto updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
