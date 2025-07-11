package com.ecommerce.user.services;

import com.ecommerce.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakAdminService {
    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-uid}")
    private String clientUId;

    private final RestTemplate restTemplate = new RestTemplate();

//    public String getAdminAccessToken() {
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("client_id", clientId);
//        params.add("username", adminUsername);
//        params.add("password", adminPassword);
//        params.add("grant_type", "password");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
//        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
//
//        return response.getBody().get("access_token").toString();
//    }

    public String getAdminAccessToken() {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", clientId);
            params.add("username", adminUsername);
            params.add("password", adminPassword);
            params.add("grant_type", "password");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

            String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            return response.getBody().get("access_token").toString();

        } catch (Exception e) {
            System.err.println("Failed to get token: " + e.getMessage());
            e.printStackTrace(); // TEMP log
            throw new RuntimeException("Keycloak token retrieval failed", e);
        }
    }


    public String createKeycloakUser(String token, UserRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", request.getUsername());
        userPayload.put("email", request.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("firstName", request.getFirstName());
        userPayload.put("lastName", request.getLastName());

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", request.getPassword());
        credentials.put("temporary", false);

        userPayload.put("credentials", List.of(credentials));

        HttpEntity<Map<String, Object>> entity =  new HttpEntity<>(userPayload, headers);

//        String url = "http://localhost:8443/admin/realms/ecom-app/users";
        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw new RuntimeException("Failed to create user in keycloak " + response.getBody());
        }

        // Extract user id from keycloak
        URI location = response.getHeaders().getLocation();
        if (location == null) throw new RuntimeException("Keycloak did not return location header " + response.getBody());
        String path = location.getPath();

        return path.substring(path.lastIndexOf("/") + 1);
    }

    private Map<String, Object> getRealmRoleRepresentation(String token, String roleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = keycloakServerUrl + "/admin/realms/" +realm + "/clients/"+ clientUId + "/roles/" + roleName;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }

    public void assignRoleNameToUser(String username, String roleName, String userId) {
        String token = getAdminAccessToken();
        Map<String, Object> roleRep = getRealmRoleRepresentation(token, roleName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(List.of(roleRep), headers);

        String url = keycloakServerUrl + "/admin/realms/" + realm +"/users/" + userId + "/role-mappings/clients/" + clientUId;
        ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful())
            throw new RuntimeException(
                    "Fail to assign role " + roleName
                            + " to user. " + username
                            + ": HTTP " + response.getStatusCode()
            );
    }
}
