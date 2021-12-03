package com.simple.bank.controller;


import com.simple.bank.repository.*;
import com.simple.bank.security.JWTTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    CustomerAccountRepo customerAccountRepo;


    @Autowired
    JWTTokenHelper jwtTokenHelper;

    /**
     * delete employee by employee id
     * @param employeeID
     * @return
     */
    @GetMapping("/deleteEmployee/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") Integer employeeID){
       employeeRepo.deleteById(employeeID);

       return new ResponseEntity(HttpStatus.OK);
    }

}
