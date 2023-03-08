package com.alvas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvas.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
