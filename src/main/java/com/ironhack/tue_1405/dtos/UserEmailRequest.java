package com.ironhack.tue_1405.dtos;

import jakarta.validation.constraints.Email;

public class UserEmailRequest {
    @Email(message = "Email should be valid")
    private String email;

    public UserEmailRequest() {
    }

    public UserEmailRequest(String email) {
        this.email = email;
    }

    public @Email(message = "Email should be valid") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email should be valid") String email) {
        this.email = email;
    }
}
