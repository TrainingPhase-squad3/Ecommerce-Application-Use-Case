package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct,Long> {

}
