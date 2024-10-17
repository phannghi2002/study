package com.rs.demo2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc // co the tao duoc mockMVC -> dung cho layer Controller
@Testcontainers //  dung de ket noi den cac CSDL chua ben trong docker
public class UserControllerIntegrationTest {
    @Container // khoi tao CSDL de co the connect toi khi test
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource // lay properties de co the connect den CSDL
    static void configureDatasource(DynamicPropertyRegistry registry) {
        //bieu thuc lamda
        // registry.add("spring.datasource.url", ()->MY_SQL_CONTAINER.getJdbcUrl());

//method reference
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc; // ket noi den controller

    //khoi tao 2 doi tuong nay boi vi ham createUser can co dau vao la userCreateRequest
    //va dau ra la userReponse
    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;

    //ham duoi day de tao gia tri cho dau vao va dau ra, ham nay duoc chay dau tien
    //truoc cac ham @Test
    @BeforeEach
    void initData() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        userCreateRequest = UserCreateRequest.builder()
                .userName("join")
                .firstName("last")
                .lastName("jackson")
                .password("123456")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cdfadfa845")
                .userName("join")
                .firstName("last")
                .lastName("jackson")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        //GIVEN: du lieu dau vao ta da biet truoc va du doan no se xay ra nhu vay: userCreateRequest, userResponse
        ObjectMapper objectMapper = new ObjectMapper();
        //vi object khong the chuyen doi localDate sang String nen ta can
        //cai them cai registerModule de co the chuyen doi no
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreateRequest);

        //khong can dung den userService vi ta da goi truc tiep den database luon

        var response = mockMvc.perform(MockMvcRequestBuilders
                        //goi den method post la WHEN
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) //kieu du lieu la dang JSON
                        //vi content chi chap nhan dang chuoi nen ta can chuyen doi tu userCreateRequest (no thuc chat la Object)
                        //do ta dinh nghia nen ta can chuyen doi Object ve String.
                        .content(content))
                //2 cai duoi nay la THEN
                .andExpect(MockMvcResultMatchers.status().isOk()) //trang thai cua HTTP STATUS: 200
                //la ket qua ta tra ve trong api
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.userName").value("join"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("last"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("jackson")


                );

        log.info("Result: {}", response.andReturn().getResponse().getContentAsString());

    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        //GIVEN
        userCreateRequest.setUserName("joi");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreateRequest);


        //WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) //badRequest: 400
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1002))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 4 characters")
                );
    }
}
