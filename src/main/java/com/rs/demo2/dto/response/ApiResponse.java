package com.rs.demo2.dto.response;

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
    //khi tra ve ket qua la ApiResponse neu nguoi dung dung builder va khong gan gia tri cua code thi no mac dinh tra ve 1000
    @Builder.Default int code = 1000;
    String message;
    T result;
}
