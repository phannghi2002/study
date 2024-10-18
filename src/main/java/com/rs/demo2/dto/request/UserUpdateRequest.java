package com.rs.demo2.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
	String password;
	String firstName;
	String lastName;
	LocalDate dob;

	List<String> roles;
}
