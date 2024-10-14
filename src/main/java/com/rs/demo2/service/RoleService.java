package com.rs.demo2.service;

import com.rs.demo2.dto.request.PermissionRequest;
import com.rs.demo2.dto.request.RoleRequest;
import com.rs.demo2.dto.response.PermissionResponse;
import com.rs.demo2.dto.response.RoleResponse;
import com.rs.demo2.entity.Permission;
import com.rs.demo2.entity.Role;
import com.rs.demo2.mapper.PermissionMapper;
import com.rs.demo2.mapper.RoleMapper;
import com.rs.demo2.repository.PermissionRepository;
import com.rs.demo2.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Permissions;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor //thay the Autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;


    public RoleResponse create(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        //vi findAllById cua repository return ve list ma permission trong Role la set nen khi gan gia tri ta can chuyen no ve dang set
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions( new HashSet<>(permissions));
      //  role.setPermissions( permissions);
        roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
