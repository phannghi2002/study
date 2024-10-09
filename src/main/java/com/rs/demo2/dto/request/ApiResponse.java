package com.rs.demo2.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //THANG NAO NULL THI THANG DAY SE KO DUOC IN RA KHI RESPONSE
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    int code = 1000;
    String message;
    T result;
}
