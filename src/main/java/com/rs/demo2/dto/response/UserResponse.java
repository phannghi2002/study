package com.rs.demo2.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	String id;
	String userName;
	String firstName;
	String lastName;
	LocalDate dob;

	Set<RoleResponse> roles;
}
