package com.rs.demo2.service;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;
    private User user;

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
                .id("cdfadfa84")
                .userName("join")
                .firstName("last")
                .lastName("jackson")
                .dob(dob)
                .build();

//password no co dang ma hoa nen ta khong can cho no vao cung duoc
        user = User.builder()
                .id("cdfadfa84")
                 .userName("join")
                .firstName("last")
                .lastName("jackson")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        //GIVEN
    //userRepository.existsByUserName(request.getUserName())
        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(false);
      //userRepository.save(user)
        Mockito.when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(userCreateRequest);

        //THEN
        //ta dung Assertions.assertThat cho unit test de hy vong gia tri con dung expect cho
        //integration tests dung cho MockMVC de hy vong ia tri cua controller
        Assertions.assertThat(response.getId()).isEqualTo("cdfadfa84");
        Assertions.assertThat(response.getUserName()).isEqualTo("join");
        Assertions.assertThat(response.getFirstName()).isEqualTo("last");
        Assertions.assertThat(response.getLastName()).isEqualTo("jackson");
        Assertions.assertThat(response.getDob()).isEqualTo("1990-01-01");
    }

    @Test
    void createUser_userExisted_fail(){
        //GIVEN

        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(true);

        //o tren xay ra loi nen nhung cau lenh o duoi ko duoc thuc hien do do khong can viet code vao
    /*      Mockito.when(userRepository.save(any())).thenReturn(user);

        //WHEN
        var response = userService.createUser(userCreateRequest);

        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("cdfadfa84");
        Assertions.assertThat(response.getUserName()).isEqualTo("join");
        Assertions.assertThat(response.getFirstName()).isEqualTo("last");
        Assertions.assertThat(response.getLastName()).isEqualTo("jackson");
        Assertions.assertThat(response.getDob()).isEqualTo("1990-01-01");

    */
//WHEN
        // bieu thuc lamda chua function thuc thi du kien tao ra ngoai le:()-> userService.createUser(userCreateRequest)
       var exception = assertThrows(AppException.class, ()-> userService.createUser(userCreateRequest));

       Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1000);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("User already existed");


    }

    @Test
    @WithMockUser(username = "join") //giup ta pass security, thay vi dung ma token de xac thuc (dung username trong
        // JWT la gia tri duy nhat ta luu vao sub  )
    void getInfoFromToken_valid_success(){
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.of(user));

        UserResponse response = userService.getInfoFromToken();

        Assertions.assertThat(response.getId()).isEqualTo("cdfadfa84");
        Assertions.assertThat(response.getUserName()).isEqualTo("join");
        Assertions.assertThat(response.getFirstName()).isEqualTo("last");
        Assertions.assertThat(response.getLastName()).isEqualTo("jackson");
        Assertions.assertThat(response.getDob()).isEqualTo("1990-01-01");
    }

    @Test
    @WithMockUser(username = "join")
    void getInfoFromToken_userNotExist_fail(){
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, ()-> userService.getInfoFromToken());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("User not existed");

    }
}
