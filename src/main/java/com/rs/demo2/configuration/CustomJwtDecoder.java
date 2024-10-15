package com.rs.demo2.configuration;

import com.nimbusds.jose.JOSEException;
import com.rs.demo2.dto.request.IntrospectRequest;
import com.rs.demo2.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            //neu dung ham nay nay se loi: authenticationService.introspect(token), vi ta can truyen IntrospectRequest vao chu khong phai truyen
            // token vao, nen ta dung builder de tao va gan gia tri

            //neu khong dung builder thi ta co the dung nhu sau:  IntrospectRequest introspectRequest = new IntrospectRequest();
            //            introspectRequest.setToken(token);
            // roi goi den authenticationService.introspect(introspectRequest);

            var response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());

            if (!response.isValid())
                throw new BadJwtException("Token is valid");

        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }

        //muc dich phai tao ham nay la ta phai kiem tra token lai 1 lan nua ke ca token dung, phan quyen va
        //con thoi han hay khong, va muc dich quan trong nhat la luu vao SecurityContext
        if (Objects.isNull(nimbusJwtDecoder)) {

            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();

        }
        log.info("nimbus" + nimbusJwtDecoder.decode(token));
        return nimbusJwtDecoder.decode(token);

    }
}
