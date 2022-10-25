package com.raiot.raiotprojects.models;

public class UserModel {

    // Needed for login, #9
    private String email;
    private String password;

    private String name;
    private String lastname;
    private Integer age;
    private String photo;

    public String getEmail() {
        return email;
    }
}