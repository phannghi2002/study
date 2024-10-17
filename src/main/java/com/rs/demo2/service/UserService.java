package com.rs.demo2.service;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;
import com.rs.demo2.enums.Role;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.exception.ErrorCode;
import com.rs.demo2.mapper.UserMapper;
import com.rs.demo2.repository.RoleRepository;
import com.rs.demo2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor //thay the Autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//makeFinal nghia la neu khong dinh nghia gi het thi no tu dong ding nghia la final
public class UserService {
    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleRepository roleRepository;

    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
//            throw new RuntimeException("User already exist");

//khi ta dinh nghia 1 exception thi se co 2 loai:1 loai la do nguoi dung tu dinh nghia va
//loai con lai la duoc built tu exception: loai nay lai duoc chia ra lam 2 loai nho hon la
// loai check exception (nhu Exception, IOException, SQLException ...) va loai con lai la
// uncheck exception (nhu RuntimeException ...). Voi loai check exception khi nem loi throw
// thi ben ngoai ta phai dung den tu khoa throws con loai con lai chi nem loi throw la du.
//Doi voi excep do nguoi dung dinh nghia thi phu thuoc vao khi ta extends Exception thuoc loai
//nao, ung voi loai nao thi ta can co cach nem throw va throws rieng biet.
            throw new AppException(ErrorCode.USER_EXISTED);

            // throw new RuntimeException();
//            throw new RuntimeException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        //cach dung thong thuong
//        User user = new User();
//        user.setUserName(request.getUserName());
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        //su dung builder
//        User user = User.builder()
//                .userName(request.getUserName())
//                .password(request.getPassword())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .dob(request.getDob())
//                .build();

        //dung mapper
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));

    }

    //dung voi permission thi dung hasAuthority boi vi ko co them ROLE
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public List<UserResponse> getAllUser() {
        log.info("in method get users");
        return userMapper.toUserResponse(userRepository.findAll());
    }

    //nay la duoc dung cho cai dk la vua co id cua nguoi dung, vua co token phu hop voi id cua nguoi dung
    //POST khac Pre o cho la POST se thuc hien cau lenh ben trong roi moi kiem tra dieu kien phu hop, neu dung moi tra ve ket qua
    //post dung cho truong hop lay thong tin cua ban than ra, nguoi khac khong the lay duoc thong tin cua minh
    //Pre kiem tra dieu kien neu dung thi moi thuc hien cau lenh
    @PostAuthorize("returnObject.userName == authentication.name")
    public UserResponse getSingleUser(String userID) {
        log.info("Method is working");
        return userMapper.toUserResponse(userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse getInfoFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {

        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            log.info("user" + user);
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {

            List<com.rs.demo2.entity.Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(new HashSet<>(roles));

        }
        //use mapper
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        //chu y khi update user voi postman thi cho du cac truong trong UserUpdateRequest co thieu truong nao
        //di chang nua neu ko du ca 2 truong la roles va password thi se loi, boi vi 2 truong do ta dung no
        //o duoi, cai nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
        //co tac dung la vang truong nao thi truong do ko bi set ve null thoi chu 2 truong nay van phia bat buoc


//        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //co the kiem tra neu co password thi ta update, or co roles truyen vao thi ta update cung duoc.


//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));


        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
