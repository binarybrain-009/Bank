package com.simple.bank.controller;

import com.simple.bank.entity.*;
import com.simple.bank.model.DepositMoney;
import com.simple.bank.model.DepositMoneyOnAccount;
import com.simple.bank.model.UserPdfData;
import com.simple.bank.repository.*;
import com.simple.bank.service.CustomerService;
import com.simple.bank.service.PdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private CustomerAccountRepo customerAccountRepo;

    @Autowired
    private CustomerService customerService;


    /**
     * create customer account
     * @param customer
     * @return customer
     */
    @PostMapping("/createCustomer")
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer) {
        CustomerEntity response = customerRepo.save(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * create bank account
     * @param account
     * @return account
     */
    @PostMapping("/createAccount")
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity account) {
        AccountEntity response = accountRepo.save(account);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * update customer details
     * @param customer
     * @return customer
     */
    @PostMapping("/updateCustomer")
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customer) {
        CustomerEntity response = customerRepo.save(customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * get customer details by customer id
     * @param customerID
     * @return customer
     */
    @GetMapping("/getCustomerDetails/{id}")
    public ResponseEntity<CustomerEntity> getCustomerDetails(@PathVariable("id") Integer customerID) {

        Optional<CustomerEntity> response = customerRepo.findById(customerID);

        if (response.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        }

    }

    /**
     * delete customer by id
     * @param customerID
     * @return
     */
    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Integer customerID) {

        customerRepo.deleteById(customerID);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * link customer with their account
     * @param customerID
     * @param accountId
     * @return
     */
    @GetMapping("/linkAccountToCustomer/{customerId}/{accountId}")
    public ResponseEntity linkAccountToCustomer(@PathVariable("customerId") Integer customerID, @PathVariable("accountId") Integer accountId) {

        Optional<CustomerEntity> customer = customerRepo.findById(customerID);
        Optional<AccountEntity> account = accountRepo.findById(accountId);

        if (customer.isEmpty()) {
            return new ResponseEntity<>("CUSTOMER_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else if (account.isEmpty()) {
            return new ResponseEntity<>("ACCOUNT_NOT_FOUND", HttpStatus.NOT_FOUND);
        } else {

            CustomerAccountEntity customerAccount = new CustomerAccountEntity();
            customerAccount.setCustomerID(customerID);
            customerAccount.setAccountTypeID(accountId);
            customerAccount.setDeposit(0.0);

            CustomerAccountEntity response = customerAccountRepo.save(customerAccount);
            return new ResponseEntity<>(HttpStatus.OK);

        }

    }

    /**
     * transfer money from account
     * @param depositMoney
     * @return
     */
    @PostMapping("/transferMoney")
    public ResponseEntity transferMoney(@RequestBody DepositMoney depositMoney) {

        ResponseEntity responseEntity = customerService.transferMoney(depositMoney);

        return responseEntity;

    }

    /**
     * money deposite
     * @param depositMoneyOnAccount
     * @return customer account
     */
    @PostMapping("/depositMoney")
    public ResponseEntity depositMoney(@RequestBody DepositMoneyOnAccount depositMoneyOnAccount) {

        Optional<CustomerAccountEntity> customerAccount = customerAccountRepo.findById(depositMoneyOnAccount.getAccountID());

        if(customerAccount.isEmpty()){
            return new ResponseEntity("ACCOUNT_NOT_FOUND",HttpStatus.NOT_FOUND);
        }else{
            customerAccount.get().setDeposit(depositMoneyOnAccount.getTransferAmount());
            CustomerAccountEntity response = customerAccountRepo.save(customerAccount.get());

            return new ResponseEntity(response, HttpStatus.OK);
        }

    }

    /**
     * generate report
     * @param accountID
     * @param customerID
     * @param response
     * @throws IOException
     */
    @GetMapping("/generateReport/{accountID}/{customerID}")
    public void generateReport(@PathVariable("accountID") Integer accountID, @PathVariable("customerID") Integer customerID  , HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<UserPdfData> listUsers = customerService.getUserDetailsForAccountStatement(customerID, accountID);

        PdfExporter exporter = new PdfExporter(listUsers);
        exporter.export(response);
    }

}
