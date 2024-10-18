package com.rs.demo2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rs.demo2.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	//    public User findByUserName(String userName);
	boolean existsByUserName(String userName);

	Optional<User> findByUserName(String userName);
}
