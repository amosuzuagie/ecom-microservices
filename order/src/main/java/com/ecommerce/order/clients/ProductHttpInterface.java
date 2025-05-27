package com.ecommerce.order.clients;

import com.ecommerce.order.dto.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Optional;

@HttpExchange
public interface ProductHttpInterface {
    @GetExchange("/api/products/{id}")
    Optional<Product> getProduct(@PathVariable String id);
}
