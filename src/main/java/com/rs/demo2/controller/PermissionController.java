package com.rs.demo2.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.rs.demo2.dto.request.PermissionRequest;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.dto.response.PermissionResponse;
import com.rs.demo2.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {

	PermissionService permissionService;

	@PostMapping
	ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
		return ApiResponse.<PermissionResponse>builder()
				.result(permissionService.create(request))
				.build();
	}

	@GetMapping
	ApiResponse<List<PermissionResponse>> getPermissionAll() {
		return ApiResponse.<List<PermissionResponse>>builder()
				.result(permissionService.getAll())
				.build();
	}

	@DeleteMapping("/{permissionId}")
	ApiResponse<Void> deletePermission(@PathVariable String permissionId) {
		permissionService.delete(permissionId);

		return ApiResponse.<Void>builder().build();
	}
}
