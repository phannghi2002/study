package com.rs.demo2.controller;

import com.rs.demo2.dto.request.PermissionRequest;
import com.rs.demo2.dto.request.RoleRequest;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.dto.response.PermissionResponse;
import com.rs.demo2.dto.response.RoleResponse;
import com.rs.demo2.service.PermissionService;
import com.rs.demo2.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getRoleAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> deleteRole(@PathVariable String roleId) {
        roleService.delete(roleId);

        return ApiResponse.<Void>builder()
                .build();
    }
}
