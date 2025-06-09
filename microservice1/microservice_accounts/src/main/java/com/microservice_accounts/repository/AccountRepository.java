package com.microservice_accounts.repository;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.microservice_accounts.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByCustomerId(Long customerId);
	Optional<Account> findByAccountNumber(Long accountNumber);

	@Transactional
	@Modifying
	void deleteByCustomerId(Long customerId);
}
