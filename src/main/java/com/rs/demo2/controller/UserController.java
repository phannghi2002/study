package com.rs.demo2.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.repository.UserRepository;
import com.rs.demo2.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

	UserService userService;
	UserRepository userRepository;

	@PostMapping("/users")
	ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {
		return ApiResponse.<UserResponse>builder()
				.result(userService.createUser(request))
				.build();
	}

	@GetMapping("/users")
	ApiResponse<List<UserResponse>> getAllUser() {

		var authentication = SecurityContextHolder.getContext().getAuthentication();

		log.info("Username: {}", authentication.getName()); // getName tuong ung voi sub trong jwt.io
		log.info("authentication contains: " + authentication);
		authentication
				.getAuthorities()
				.forEach(grantedAuthority ->
						log.info(grantedAuthority.getAuthority())); // getAuthorities giong scope trong jwt.io

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

		return ApiResponse.<String>builder().result("Delete user success").build();
	}
}
