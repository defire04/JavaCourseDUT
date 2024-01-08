package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User {
    private long id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;
    private Address address;
    private int __v;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Name {
        private String firstname;
        private String lastname;

    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Address {
        private Geolocation geolocation;
        private String city;
        private String street;
        private int number;
        private String zipcode;

    }
    @Data
    @NoArgsConstructor
    @ToString
    public static class Geolocation {
        private String lat;
        @JsonProperty("long")
        private String lon;

    }
}
