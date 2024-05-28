package com.ironhack.tue_1405.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;

// DTO Data Transfer Object - Design Pattern
public class UserUpdateRequest {

    private String name;

    @Min(value = 0, message = "Age must be greater than 0")
    private Integer age;

    @Email(message = "Email should be valid")
    private String email;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Min(value = 0, message = "Age must be greater than 0") Integer getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "Age must be greater than 0") Integer age) {
        this.age = age;
    }

    public @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") String email) {
        this.email = email;
    }
}
