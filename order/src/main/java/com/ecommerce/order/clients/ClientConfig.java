package com.ecommerce.order.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public ProductHttpInterface productServiceClientConfig(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("http://product-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((req, res) -> Optional.empty()))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(ProductHttpInterface.class);
    }

    @Bean
    public UserHttpInterface userServiceClientConfig(RestClient.Builder builder) {
        RestClient restClient = builder.baseUrl("http://user-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((req, res) -> Optional.empty()))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserHttpInterface.class);
    }


}
