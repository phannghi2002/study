package com.rs.demo2.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //THANG NAO NULL THI THANG DAY SE KO DUOC IN RA KHI RESPONSE
public class ApiResponse<T> {
    private int code = 1000;
    private String message;
    private T result;
}
