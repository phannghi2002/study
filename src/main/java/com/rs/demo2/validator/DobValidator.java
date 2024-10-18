package com.rs.demo2.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

	private int min;

	@Override
	public void initialize(DobConstraint constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);

		// lay gia tri thong qua thang min trong khi truyen qua annotation
		min = constraintAnnotation.min();
	}

	@Override
	public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
		// neu gia tri null thi van chap nhan hop le
		// vd: {
		//    "userName":"user",
		//    "password": "user",
		//    "firstName":"Phan",
		//    "lastName":"Nghi",
		//    "dob":null,
		//    "roles":["USER"]
		// }
		if (Objects.isNull(value)) return true;

		// ChronoUnit dung de thao tac voi thoi gian rat hieu qua
		long years = ChronoUnit.YEARS.between(value, LocalDate.now());

		return years >= min;
	}
}
