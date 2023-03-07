package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
