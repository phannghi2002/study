package com.rs.demo2.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rs.demo2.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	//    @ExceptionHandler(value = RuntimeException.class)
	//    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
	//        ApiResponse apiResponse = new ApiResponse<>();
	//        apiResponse.setCode(1001);
	//        apiResponse.setMessage(exception.getMessage());
	//        return ResponseEntity.badRequest().body(apiResponse);
	//    }

	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
		ApiResponse apiResponse = new ApiResponse<>();
		ErrorCode errorCode = exception.getErrorCode();
		apiResponse.setCode(errorCode.getCode());
		apiResponse.setMessage(errorCode.getMessage());
		return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {

		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ApiResponse.builder()
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.build());
	}
	//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
	//    ResponseEntity<String> handlingValidation(MethodArgumentNotValidException exception){
	//        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
	//    }

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
		// exception.getFieldError().getDefaultMessage(): tra ve message lay tu doan code duoi day: la USERNAME_INVALID
		// @Size(min =3, message = "USERNAME_INVALID")
		String enumKey = exception.getFieldError().getDefaultMessage();
		// truong hop nay tao ra de tranh khi nguoi dev ghi sai trong message de tra ve loi
		ErrorCode errorCode = ErrorCode.INVALID_KEY;

		// khi log attributes ta thay no co dang key, value -> kieu du lieu Map
		Map<String, Object> attributes = null;

		try {
			errorCode = ErrorCode.valueOf(enumKey);

			var constrainViolation =
					exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

			// attributes la tat ca gia tri ma ta truyen thong qua annotation
			attributes = constrainViolation.getConstraintDescriptor().getAttributes();
			log.info("Attribute3 {}", attributes);

		} catch (IllegalArgumentException e) {

		}

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ApiResponse.builder()
						.code(errorCode.getCode())
						.message(
								Objects.nonNull(attributes)
										? mapAttribute(errorCode.getMessage(), attributes)
										: errorCode.getMessage())
						.build());
	}

	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiResponse> handlingAnotherException() {
		ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

		return ResponseEntity.status(errorCode.getStatusCode())
				.body(ApiResponse.builder()
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.build());
	}

	private static final String MIN_ATTRIBUTE = "min";

	// ham nay dung de thay the {min} bang value ma ta lay tu attributes co key la min, neu khong tim thay
	// ki tu nao khop thi no se thay the, khong tim thay thi se khong lam gi ca, hay chinh xac hon la
	// return ve message ban dau
	private String mapAttribute(String message, Map<String, Object> attributes) {
		String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
		return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
	}
}
