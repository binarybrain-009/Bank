package com.simple.bank.repository;

import com.simple.bank.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountEntity, Integer> {
}
