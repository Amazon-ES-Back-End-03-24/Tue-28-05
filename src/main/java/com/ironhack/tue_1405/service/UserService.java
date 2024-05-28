package com.ironhack.tue_1405.service;

import com.ironhack.tue_1405.dtos.UserEmailRequest;
import com.ironhack.tue_1405.dtos.UserRequest;
import com.ironhack.tue_1405.dtos.UserUpdateRequest;
import com.ironhack.tue_1405.model.User;
import com.ironhack.tue_1405.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // receives a user and returns persisted (saved) user
    public User createUser(UserRequest receivedUser) {
        // new user instance
        // User newUser = new User(receivedUser.getEmail(),receivedUser.getAge(),receivedUser.getName());

        User newUser = new User();
        newUser.setEmail(receivedUser.getEmail());
        newUser.setAge(receivedUser.getAge());
        newUser.setName(receivedUser.getName());

        User savedUser = userRepository.save(newUser);

        // If returned DTO Response
        // UserRequest response = new UserRequest(savedUser.getName(), savedUser.getAge(), savedUser.getEmail());


        // User savedUser = userRepository.save(newUser);
        return userRepository.save(newUser);
    }

    public void updateUser(Long userId, UserUpdateRequest userRequest) {
        // TODO add not found exception handling
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (userRequest.getName() != null) {
            foundUser.setName(userRequest.getName());
        }

        if (userRequest.getAge() != null) {
            foundUser.setAge(userRequest.getAge());
        }

        if (userRequest.getEmail() != null) {
            foundUser.setEmail(userRequest.getEmail());
        }

        userRepository.save(foundUser);
    }

    public void updateUserEmail(Long userId, UserEmailRequest userEmailRequest) {
        // TODO add not found exception handling
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (userEmailRequest.getEmail() != null) {
            foundUser.setEmail(userEmailRequest.getEmail());
        }

        userRepository.save(foundUser);
    }

    public User deleteUser(Long userId) {
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.deleteById(userId);

        return foundUser;
    }
}
