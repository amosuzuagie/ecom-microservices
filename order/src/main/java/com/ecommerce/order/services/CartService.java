package com.ecommerce.order.services;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private UserRepository userRepository;

    public Boolean addToCart(String userId, CartItemRequest request) {

        //Search and validate product and product quantity.
//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if (productOpt.isEmpty())
//            return false;
//
//        Product product = productOpt.get();
//        if (product.getStockQuantity() < request.getQuantity())
//            return false;
//
//        //Search and validate user
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if (userOpt.isEmpty())
//            return false;
//
//        User user = userOpt.get();

        //Check if product already exist and update or else add new cart
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
//            existingCartItem.setPrice(product.getPrice());
            existingCartItem.setPrice(BigDecimal.valueOf(10));
//            existingCartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            existingCartItem.setTotalPrice(BigDecimal.valueOf(100));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());
//            cartItem.setPrice(product.getPrice());
            cartItem.setPrice(BigDecimal.valueOf(10));
//            cartItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItem.setTotalPrice(BigDecimal.valueOf(100));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean removeCartItem(String userId, String productId) {
//        Optional<Product> productOpt = productRepository.findById(productId);
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
