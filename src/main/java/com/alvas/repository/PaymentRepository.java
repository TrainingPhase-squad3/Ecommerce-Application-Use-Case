package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
