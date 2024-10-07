package com.rs.demo2.service;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.entity.User;
import com.rs.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreateRequest request) {
        User user = new User();

        if(userRepository.existsByUserName(request.getUserName())){
            throw new RuntimeException("User already exist");
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
