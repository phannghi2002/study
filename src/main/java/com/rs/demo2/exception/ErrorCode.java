package com.rs.demo2.exception;

//neu dinh nghia enum la object thi can dinh nghia cac field cung nhu constructor,
// dinh nghia enum basic chi can value thi khong can tao constructor
public enum ErrorCode {
    USER_EXISTED(1004, "User already existed"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    INVALID_KEY(2000,"Invalid message key" ),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters")
    ;


    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code ;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
