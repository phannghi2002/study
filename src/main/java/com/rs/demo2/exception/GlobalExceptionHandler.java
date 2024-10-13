package com.rs.demo2.exception;

import com.rs.demo2.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
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
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(
                        ApiResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<String> handlingValidation(MethodArgumentNotValidException exception){
//        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        //exception.getFieldError().getDefaultMessage(): tra ve message lay tu doan code duoi day: la USERNAME_INVALID
        // @Size(min =3, message = "USERNAME_INVALID")
        String enumKey = exception.getFieldError().getDefaultMessage();
        //truong hop nay tao ra de tranh khi nguoi dev ghi sai trong message de tra ve loi
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e){

        }
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingAnotherException() {
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
