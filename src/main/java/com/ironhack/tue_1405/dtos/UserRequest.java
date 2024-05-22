package com.ironhack.tue_1405.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO Data Transfer Object - Design Pattern
public class UserRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 0, message = "Age must be greater than 0")
    private Integer age;

    @Email(message = "Email should be valid")
    @NotNull
    private String email;

    public UserRequest(String name, Integer age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public @NotBlank(message = "Name is mandatory") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name is mandatory") String name) {
        this.name = name;
    }

    public @Min(value = 0, message = "Age must be greater than 0") Integer getAge() {
        return age;
    }

    public void setAge(@Min(value = 0, message = "Age must be greater than 0") Integer age) {
        this.age = age;
    }

    public @Email(message = "Email should be valid") @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") @NotNull String email) {
        this.email = email;
    }
}
