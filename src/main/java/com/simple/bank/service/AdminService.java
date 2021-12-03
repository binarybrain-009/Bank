package com.simple.bank.service;

import com.simple.bank.entity.CustomerAccountEntity;
import com.simple.bank.repository.CustomerAccountRepo;
import com.simple.bank.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class AdminService {

    @Autowired
    CustomerAccountRepo customerAccountRepo;

    /**
     * fixed the deposit rate
     */
    @Scheduled(fixedRate = 1000*60*60*24*365)
    public void scheduleFixedRateTask() {
        List<CustomerAccountEntity> customerEntities = customerAccountRepo.findAll();
        for (CustomerAccountEntity customer : customerEntities){
            Double interest = customer.getDeposit() * 3.5;
            customer.setDeposit(customer.getDeposit() + interest);

            customerAccountRepo.save(customer);
        }
    }
}
