package com.rs.demo2.mapper;

import com.rs.demo2.dto.request.PermissionRequest;
import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.PermissionResponse;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.Permission;
import com.rs.demo2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
//nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE no co tac dung chi update nhung truong co gia tri,
//khi khong co gia tri thi no van lay gia tri cu, thuong dung cho update data

//componentModel = "spring" dung de dua no vao Spring bean hay la tiem su phu thuoc cua no, khi can su dung chi can goi den Autowired
@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    //chuyen doi kieu du kieu tu UserCreateRequest -> User
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
