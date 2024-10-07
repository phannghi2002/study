package com.rs.demo2.controller;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.entity.User;
import com.rs.demo2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    User createUser(@RequestBody @Valid UserCreateRequest request) {
        return userService.createRequest(request);
    }

    @GetMapping("/users")
    List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    User getSingleUser(@PathVariable String userId) {
        return userService.getSingleUser(userId);
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(userId, request);

        return user;
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return "Delete success";
    }
}
