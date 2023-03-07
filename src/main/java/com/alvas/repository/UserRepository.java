package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
