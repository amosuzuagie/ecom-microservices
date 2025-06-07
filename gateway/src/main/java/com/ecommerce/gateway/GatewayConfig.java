package com.ecommerce.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r
                        .path("/products/**")
                        .filters(f -> f
                                .retry(retryConfig -> retryConfig
                                        .setRetries(4)
                                        .setMethods(HttpMethod.GET)
                                )
                                .rewritePath("/products(?<segment>/?.*)", "/api/products${segment}")
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/products")
                                )
                        )
                        .uri("lb://PRODUCT-SERVICE")
                )
                .route("user-service", r -> r
                        .path("/users/**")
                        .filters(f-> f
                                .rewritePath("users(?<segment>/?.*)", "/api/users${segment}")
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/users")
                                )
                        )
                        .uri("lb://USER-SERVICE")
                )
                .route("order-service", r -> r
                        .path("/orders/**", "/cart/**")
                        .filters(f-> f
                                .rewritePath("/(?<segment>.*)", "/api/${segment}")
                                .circuitBreaker(config -> config
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/order")
                                )
                        )
                        .uri("lb://ORDER-SERVICE"))
//                .route("eureka-server", r -> r
//                        .path("/eureka/main")
//                        .filters(f -> f.rewritePath("eureka/main", "/"))
//                        .uri("http://localhost:8761"))
//                .route("eureka-server-static", r -> r
//                        .path("/eureka/**")
//                        .uri("http://localhost:8761"))
                .build();
    }
}
