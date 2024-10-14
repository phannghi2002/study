package com.rs.demo2.configuration;

import com.rs.demo2.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {
            "users", "auth/token", "auth/introspect"
    };
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, "/users").permitAll()
//                .requestMatchers(HttpMethod.POST, "auth/token", "auth/introspect").permitAll()

        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                //default trong JWT la SCOPE .requestMatchers(HttpMethod.GET, "/users").hasAuthority("SCOPE_ADMIN")
                // hoac co the dung hasAuthority("ROLE_ADMIN")
                //hoac co the dung la hasRole("ADMIN")
                //su khac biet cua hasAuthority and hasRole la hasRole thi tu dong them tien to ROLE_ truoc khi
                //kiem tra, con hasAuthority thi khong them, khi ta kiem tra hasAuthority thi can nhap day du
                //chinh xac no moi kiem tra dung duoc
                .anyRequest().authenticated());

        //providerManager, khi dung jwtConfigurer.decoder thi Oauth2 no tu dong lay token trong Authorization header ma ta khong can phai truyen
        //token vao
        httpSecurity.oauth2ResourceServer(oath2 -> oath2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())

                //ham nay chuyen doi mot so thu trong jwt dua tren cai ta dinh nghia, neu khong dinh nghia ham nay thi khi
                //config phan quyen ta phai bat buoc them SCOPE vao: @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')") neu khong co no thi se
                //ko truy cap duoc, SCOPE_ la default, neu dung ham duoi nay thi ta thay SCOPE_ bang chuoi rong ""
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//chinh sua lai loi mac dinh trong security boi vi neu no ko duoc xac thuc (loi 401 Unauthorized) thi no se xu ly loi mac dinh cua spring security chu khong chay vao
 // phan xu ly loi trong GlobalExceptionHandler ma ta dinh nghia. tim hieu them o duong link: https://www.javaguides.net/2024/04/authenticationentrypoint-in-spring-security.html
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
       )

        ;

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    //chuyen doi thay vi dung mac dinh la SCOPE_ADMIN thi doi thanh ROLE_ADMIN
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        //khong can them ROLE_ vao truoc no nua vi da them vao ROLE chu ko them vao permission
        //thay the SCOPE_ bang ""
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
        //ham nay no se tu giai ma jwt thanh header, payload, signature  -> sau do no se tu dong xac thuc roi tra ve trong SecurityContext neu
        //xac thuc thanh cong, con sai tra ve loi 401 Unauthorized
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
