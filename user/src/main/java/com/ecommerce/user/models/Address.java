package com.ecommerce.user.models;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Address {
    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;

//    @OneToOne
//    private User user;
}
