package com.rs.demo2.controller;

import com.rs.demo2.dto.request.IntrospectRequest;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.AuthenticationResponse;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.exception.ErrorCode;
import com.rs.demo2.repository.UserRepository;
import com.rs.demo2.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/users")
    ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createRequest(request));
        return apiResponse;
    }

    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getAllUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));


        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/getInfoFromToken")
    ApiResponse<UserResponse> getInfoFromToken() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getInfoFromToken())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getSingleUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getSingleUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {

        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return ApiResponse.<String>builder()
                .result("Delete user success")
                .build();
    }
}
