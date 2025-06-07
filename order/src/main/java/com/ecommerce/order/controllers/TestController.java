package com.ecommerce.order.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @RateLimiter(name = "rateBreaker", fallbackMethod = "getTestFallback")
    public String test() {
        return "Trace works!";
    }

    public String getTestFallback(Exception exception) {
        return "Test Fallback";
    }
}
