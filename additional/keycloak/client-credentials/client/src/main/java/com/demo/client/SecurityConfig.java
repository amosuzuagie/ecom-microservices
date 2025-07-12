package com.demo.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(a -> a
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        Customizer.withDefaults()
                ));
        return http.build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService (ClientRegistrationRepository repository) {
        return new InMemoryOAuth2AuthorizedClientService(repository);
    }

    @Bean
    public OAuth2AuthorizedClientManager clientManager(ClientRegistrationRepository repository, OAuth2AuthorizedClientService clientService) {
        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(repository, clientService);
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder
                .builder().clientCredentials()
                .build();
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}
