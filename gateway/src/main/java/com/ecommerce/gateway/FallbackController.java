package com.ecommerce.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController()
public class FallbackController {

    @GetMapping("/fallback/products")
    public ResponseEntity<List<String>> productFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Product Service unavailable. Try again after some time"));
    }

    @GetMapping("/fallback/users")
    public ResponseEntity<List<String>> userFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("User Service unavailable. Try again after some time"));
    }

    @GetMapping("/fallback/order")
    public ResponseEntity<List<String>> orderFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Collections.singletonList("Order Service unavailable. Try again after some time"));
    }
}
