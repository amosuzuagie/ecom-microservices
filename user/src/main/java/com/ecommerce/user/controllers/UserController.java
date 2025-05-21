package com.ecommerce.user.controllers;


import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    //@RequestMapping(value = "/api/users", method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        return ResponseEntity.ok(userService.fetchAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest request) {
        userService.addUser(request);
        return ResponseEntity.ok("User created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUse(@PathVariable String id, @RequestBody UserRequest request) {
        boolean updated = userService.updateUser(id, request);
        if (!updated) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("User updated successfully");
    }
}
