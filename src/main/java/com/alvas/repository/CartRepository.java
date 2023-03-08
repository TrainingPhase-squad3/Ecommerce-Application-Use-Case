package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

//	Page<Cart> findByUserAndPayment_PaymentDateBetween(User user, LocalDate startDate, LocalDate endDate,
//			Pageable pageable);
//
//	long countByUserAndPayment_PaymentDateBetween(User user, LocalDate startDate, LocalDate endDate);

	
}
