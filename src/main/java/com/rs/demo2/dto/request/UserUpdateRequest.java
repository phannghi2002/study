package com.rs.demo2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
}
