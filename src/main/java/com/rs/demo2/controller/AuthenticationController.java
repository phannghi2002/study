package com.rs.demo2.controller;

import com.nimbusds.jose.JOSEException;
import com.rs.demo2.dto.request.AuthenticationRequest;
import com.rs.demo2.dto.request.IntrospectRequest;
import com.rs.demo2.dto.request.LogoutRequest;
import com.rs.demo2.dto.request.RefreshRequest;
import com.rs.demo2.dto.response.ApiResponse;
import com.rs.demo2.dto.response.AuthenticationResponse;
import com.rs.demo2.dto.response.IntrospectResponse;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var resultAuth = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(resultAuth)
                .build();
    }


    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var resultAuth = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(resultAuth)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var resultAuth = authenticationService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(resultAuth)
                .build();
    }
}
