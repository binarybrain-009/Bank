package com.simple.bank.repository;

import com.simple.bank.entity.CustomerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAccountRepo extends JpaRepository<CustomerAccountEntity, Integer> {
    CustomerAccountEntity findByAccountIDAndCustomerID(Integer id, Integer customerId);

}
