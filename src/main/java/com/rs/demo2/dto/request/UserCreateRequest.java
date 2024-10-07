package com.rs.demo2.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequest {
    @Size(min =3, message = "Username must be at least 3 characters")
    private String userName;
    @Size(min=8, message = "Password must be at least 8 characters")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
