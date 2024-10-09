package com.rs.demo2.service;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.exception.ErrorCode;
import com.rs.demo2.mapper.UserMapper;
import com.rs.demo2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //thay the Autowired
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) //makeFinal nghia la neu khong dinh nghia gi het thi no tu dong ding nghia la final
public class UserService {
    UserRepository userRepository;

    UserMapper userMapper;

    public User createRequest(UserCreateRequest request) {
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

        return userRepository.save(user);

    }

    public List<UserResponse> getAllUser() {
        return userMapper.toUserResponse(userRepository.findAll());
    }

    public UserResponse getSingleUser(String userID) {
        return userMapper.toUserResponse(userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));

        //use mapper
//        user.setPassword(request.getPassword());
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setDob(request.getDob());

        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
