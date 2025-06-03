package com.ecommerce.order.clients;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class ClientConfig {

    @Autowired(required = false)
    private ObservationRegistry observationRegistry;

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private Propagator propagator;


    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        RestClient.Builder builder = RestClient.builder();
        if (observationRegistry != null) builder.requestInterceptor(createTracingInterceptor());
        return builder;
    }

    private ClientHttpRequestInterceptor createTracingInterceptor() {
        //Distributed Tracing configuration
        return ((request, body, execution) -> {
            if (tracer != null && propagator != null && tracer.currentSpan() != null) {
                propagator.inject(
                        tracer.currentTraceContext().context(),
                        request.getHeaders(),
                        (carrier, key, value) -> carrier.add(key, value)
                );
            }
            return execution.execute(request, body);
        });
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
