package com.rs.demo2.service;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.entity.User;
import com.rs.demo2.exception.AppException;
import com.rs.demo2.exception.ErrorCode;
import com.rs.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreateRequest request)  {
        User user = new User();

        if(userRepository.existsByUserName(request.getUserName())){
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

        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);

    }

    public List<User> getAllUser (){
        return userRepository.findAll();
    }

    public User getSingleUser (String userID){
        return userRepository.findById(userID).orElseThrow(()-> new RuntimeException("User not found"));
    }

    public User updateUser (String userID, UserUpdateRequest request){
        User user = getSingleUser(userID);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public void deleteUser(String userID){
        User user = getSingleUser(userID);
        userRepository.deleteById(userID);
    }
}
