package com.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Service2Client {
    private final RestTemplate rest;
    private final OAuth2AuthorizedClientManager manager;

    @Value("${service2.url}")
    String service2Url;

    public Service2Client(RestTemplate rest, OAuth2AuthorizedClientManager manager) {
        this.rest = rest;
        this.manager = manager;
    }

    public String fetchData() {
        // Verifying Keycloak token and Generating New One
        var authRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak-client")
                .principal("machine")
                .build();

        var client = manager.authorize(authRequest);
        assert client != null;
        String token = client.getAccessToken().getTokenValue();
        System.out.println("ACCESS_TOKEN: " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var response = rest.exchange(
                service2Url + "/data",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        return response.getBody();

//        // Forwarding Token Gotten From Keycloak
//        Authentication auth = SecurityContextHolder.getContext()
//                .getAuthentication();
//        String incomingToken = null;
//
//        if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
//            incomingToken = jwtAuthenticationToken.getToken().getTokenValue();
//        }
//        System.out.println("Token Value: " + incomingToken);
//
//        HttpHeaders headers = new HttpHeaders();
//        assert incomingToken != null;
//        headers.setBearerAuth(incomingToken);
//
//        var response = rest.exchange(
//                service2Url + "/data",
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                String.class
//        );
//        return response.getBody();
    }
}
