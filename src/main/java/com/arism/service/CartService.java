package com.arism.service;

import com.arism.dto.CartDto;
import com.arism.exception.InsufficientStockException;
import com.arism.exception.ResourceNotFoundException;
import com.arism.mapper.CartMapper;
import com.arism.model.Cart;
import com.arism.model.CartItem;
import com.arism.model.Product;
import com.arism.model.User;
import com.arism.repository.CartRepository;
import com.arism.repository.ProductRepository;
import com.arism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDto addToCart(Long userId, Long productId, Integer quantity) throws InsufficientStockException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock available for item %s.", product.getName());
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(null, user, new ArrayList<>()));

        Optional<CartItem> existingCartItems = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
        if (existingCartItems.isPresent()) {
            CartItem cartItem = existingCartItems.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
          CartItem cartItem = new CartItem(null, cart, product, quantity);
          cart.getItems().add(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    public CartDto getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
