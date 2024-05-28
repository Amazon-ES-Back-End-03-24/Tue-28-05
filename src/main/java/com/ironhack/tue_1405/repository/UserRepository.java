package com.ironhack.tue_1405.repository;

import com.ironhack.tue_1405.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContaining(String name);

    List<User> findByNameAndAge(String name, Integer age);

    List<User> findByAge(Integer age);
}
