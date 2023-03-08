package com.alvas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);
}
