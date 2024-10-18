package com.rs.demo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rs.demo2.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
