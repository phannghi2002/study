package com.rs.demo2.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequest {
    //tai sao o day ta khong dung message = ErrorCode.USERNAME_INVALID.getMessage() ma lai phai gan qua enumKey
    //roi tu enumKey nay ta lai moi lay getCode hay getMessage de tra ve response. Tai vi message trong size no
    //bat buoc gia tri la constant khong phai la value co the thay doi.
    @Size(min =3, message = "USERNAME_INVALID")

    private String userName;
    @Size(min=8, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
}
