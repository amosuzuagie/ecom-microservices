package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductHttpInterface;
import com.ecommerce.order.clients.UserHttpInterface;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.Product;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductHttpInterface productHttpInterface;
    private final UserHttpInterface userHttpInterface;

    @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallback")
    public Boolean addToCart(String userId, CartItemRequest request) {

        // Search and validate product and product quantity.
        Optional<Product> productOpt = productHttpInterface.getProduct(request.getProductId());
        if (productOpt.isEmpty())
            return false;

        Product product = productOpt.get();
        if (product.getStockQuantity() < request.getQuantity())
            return false;

        //Validate Users existence
        UserResponse userResponse = userHttpInterface.getUserDetails(userId);
        if (userResponse == null) return false;

        //Check if product already exist and update or else add new cart
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice());
            existingCartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(String.valueOf(product.getId()));
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean removeCartItem(String userId, String productId) {
        System.out.println("User: " +userId + " Product: " +productId);
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        System.out.println("User ID: " +userId);
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        System.out.println(items);
        return items;
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public Boolean addToCartFallback(String userId, CartItemRequest request, Exception exception) {
        exception.printStackTrace();
        System.out.println("FALLBACK CALLED");
        return false;
    }
}
