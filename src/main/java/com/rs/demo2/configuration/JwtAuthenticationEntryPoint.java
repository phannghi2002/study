package com.rs.demo2.configuration;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.exception.ErrorCode;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

		response.setStatus(errorCode.getStatusCode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE); // response tra ve dang JSON

		ApiResponse<?> apiResponse = ApiResponse.builder()
				.code(errorCode.getCode())
				.message(errorCode.getMessage())
				.build();

		ObjectMapper objectMapper = new ObjectMapper();
		// write can truyen vao la 1 string chu khong phai la object, vi vay ta can dung objectMapper de chuyen doi
		// thanh string
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
		response.flushBuffer(); // gui du lieu den client ngay lap tuc bang cach flushBuffer (xoa bo dem)
	}
}
