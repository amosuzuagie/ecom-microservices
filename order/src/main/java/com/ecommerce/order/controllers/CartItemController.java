package com.ecommerce.order.controllers;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cart")
public class CartItemController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request) {
        boolean addedToCart = cartService.addToCart(userId, request);
        return addedToCart ?
                ResponseEntity.status(HttpStatus.CREATED).build() :
                ResponseEntity.badRequest().body("Request Not Completed | Product Out of Stock, User or Product not found.");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable String productId) {
        boolean deleted = cartService.removeCartItem(userId, productId);
        System.out.println("Checking Delete Value: " + deleted );
        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
