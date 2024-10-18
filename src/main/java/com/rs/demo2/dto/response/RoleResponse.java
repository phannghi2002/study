package com.rs.demo2.dto.response;

import java.util.Set;

import com.rs.demo2.entity.Permission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {
	String name;
	String description;
	Set<Permission> permissions;
}
