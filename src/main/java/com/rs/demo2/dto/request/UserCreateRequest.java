package com.rs.demo2.dto.request;

import com.rs.demo2.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    //tai sao o day ta khong dung message = ErrorCode.USERNAME_INVALID.getMessage() ma lai phai gan qua enumKey
    //roi tu enumKey nay ta lai moi lay getCode hay getMessage de tra ve response. Tai vi message trong size no
    //bat buoc gia tri la constant khong phai la value co the thay doi.
    @Size(min =4, message = "USERNAME_INVALID")
    String userName;

    @Size(min=4, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 18, message = "BIRTHDAY_INVALID")
    LocalDate dob;
}
