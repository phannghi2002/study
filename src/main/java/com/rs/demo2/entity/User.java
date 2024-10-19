package com.rs.demo2.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	String id;

	//COLLATE utf8mb4_unicode_ci la khong phan biet chu hoa va chu thuong
	@Column(name = "userName", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
	String userName;
	String password;
	String firstName;
	String lastName;
	LocalDate dob;

	@ManyToMany
	Set<Role> roles;
}
