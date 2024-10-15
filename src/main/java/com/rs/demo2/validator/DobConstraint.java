package com.rs.demo2.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD}) // chi cau hinh no danh cho cac field
@Retention(RetentionPolicy.RUNTIME) // thuc hien annotation nay khi run time
@Constraint(
        validatedBy = { DobValidator.class } //logic xac thuc la dung class DobValidator
)
public @interface DobConstraint {
    //Khi khai bao @DobConstraint thi ta can truyen cac gia tri duoi day: message, min, groups, payload
    //nhung bat buoc phai co min boi vi ta khong khoi tao gia tri mac dinh cho no

    String message() default "Invalid date of birth";

    int min ();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
