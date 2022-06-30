package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
			
	Account findAccountByUsername(String Username);
}
