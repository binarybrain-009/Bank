package com.simple.bank.service;

import com.simple.bank.entity.AccountEntity;
import com.simple.bank.entity.CustomerAccountEntity;
import com.simple.bank.entity.CustomerEntity;
import com.simple.bank.model.DepositMoney;
import com.simple.bank.model.UserPdfData;
import com.simple.bank.repository.AccountRepo;
import com.simple.bank.repository.CustomerAccountRepo;
import com.simple.bank.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    CustomerAccountRepo customerAccountRepo;


    public ResponseEntity transferMoney(DepositMoney depositMoney) {

        Optional<CustomerEntity> sender = customerRepo.findById(depositMoney.getSenderID());

        Optional<CustomerEntity> receiver = customerRepo.findById(depositMoney.getReceiverID());

        CustomerAccountEntity senderAccount = customerAccountRepo.findByAccountIDAndCustomerID(depositMoney.getSenderAccountNumber(), depositMoney.getSenderID());

        CustomerAccountEntity receiverAccount = customerAccountRepo.findByAccountIDAndCustomerID(depositMoney.getReceiverAccountNumber(), depositMoney.getReceiverID());

        if (sender.isEmpty()) {
            return new ResponseEntity<>("SENDER_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else if (receiver.isEmpty()) {
            return new ResponseEntity<>("RECEIVER_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else if (senderAccount == null) {
            return new ResponseEntity<>("SENDER_ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else if (receiverAccount == null) {
            return new ResponseEntity<>("RECEIVER_ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else {

            Double senderMoney = senderAccount.getDeposit();

            if (senderMoney < depositMoney.getTransferAmount()) {

                return new ResponseEntity<>("NOT_ENOUGH_MONEY_FOR_TRANSFER", HttpStatus.OK);
            }

            senderAccount.setDeposit(senderAccount.getDeposit() - depositMoney.getTransferAmount());

            customerAccountRepo.save(senderAccount);

            receiverAccount.setDeposit(receiverAccount.getDeposit() + depositMoney.getTransferAmount());

            customerAccountRepo.save(receiverAccount);

            return new ResponseEntity<>(HttpStatus.OK);

        }
    }

    /**
     * get user details for account
     * @param customerID
     * @param accountID
     * @return
     */
    public List<UserPdfData> getUserDetailsForAccountStatement(Integer customerID, Integer accountID){

        List<UserPdfData> userPdfDataList = new ArrayList<>();
        UserPdfData user = new UserPdfData();
        CustomerAccountEntity customerAccount = customerAccountRepo.findByAccountIDAndCustomerID(accountID, customerID);
        Optional<CustomerEntity> customer = customerRepo.findById(customerID);

        if(customerAccount != null && !customer.isEmpty()){
            user.setUserAccountNumber(customerAccount.getAccountID());
            user.setUserName(customer.get().getName());

            Optional<AccountEntity> accountEntity = accountRepo.findById(customerAccount.getAccountTypeID());

            user.setUserAccountName(accountEntity.get().getAccountName());
            user.setDepositAmount(customerAccount.getDeposit());

            userPdfDataList.add(user);
//            userPdfDataList.add(user);
        }

        return userPdfDataList;
    }
}
