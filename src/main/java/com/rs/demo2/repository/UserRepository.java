package com.rs.demo2.repository;

import com.rs.demo2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
//    public User findByUserName(String userName);
    boolean existsByUserName(String userName);
}
