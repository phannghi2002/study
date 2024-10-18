package com.rs.demo2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rs.demo2.dto.request.RoleRequest;
import com.rs.demo2.dto.response.RoleResponse;
import com.rs.demo2.entity.Role;

// componentModel = "spring" dung de dua no vao Spring bean hay la tiem su phu thuoc cua no, khi can su dung chi can goi
// den Autowired
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
	// chuyen doi kieu du kieu tu UserCreateRequest -> User
	@Mapping(target = "permissions", ignore = true)
	Role toRole(RoleRequest request);

	RoleResponse toRoleResponse(Role role);
}
