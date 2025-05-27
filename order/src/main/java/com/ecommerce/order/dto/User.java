package com.ecommerce.order.dto;

import lombok.Data;

@Data
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
//    private UserRole role;
//    private AddressDTO address;
}
