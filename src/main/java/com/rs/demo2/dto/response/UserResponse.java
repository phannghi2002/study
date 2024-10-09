package com.rs.demo2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
}
