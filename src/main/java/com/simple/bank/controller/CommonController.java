package com.simple.bank.controller;

import com.simple.bank.entity.EmployeeEntity;
import com.simple.bank.entity.Role;
import com.simple.bank.entity.UserProfile;
import com.simple.bank.repository.*;
import com.simple.bank.requestDto.EmployeeDto;
import com.simple.bank.requestDto.LoginDto;
import com.simple.bank.requestDto.LoginResponseDto;
import com.simple.bank.security.JWTTokenHelper;
import com.simple.bank.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CommonController {

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    CustomerAccountRepo customerAccountRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenHelper jwtTokenHelper;

    /**
     * admin and employee signup
     * @param loginDto
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @PostMapping("/signup")
    public ResponseEntity<?> authenticateEmployee(@RequestBody LoginDto loginDto) throws NoSuchAlgorithmException, InvalidKeyException {
        try{
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String token = jwtTokenHelper.generateToken(userDetails.getUsername());

            LoginResponseDto loginResponseDto = new LoginResponseDto(token);

            return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Invalid username and password", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * create employee account
     * @param employeeDto
     * @return
     */
    @PostMapping("/createEmployee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
        if (userProfileRepository.findByEmail(employeeDto.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setName(employeeDto.getName());
        employeeEntity.setContactNumber(employeeDto.getContactNumber());
        employeeEntity.setBirthdate(employeeDto.getBirthday());

        Set<Role> roles = new HashSet<>();

        if (employeeDto.getRole() == null) {
            return new ResponseEntity<>("Role is not Found", HttpStatus.BAD_REQUEST);
        } else {
            Role role = roleRepository.findByRole(employeeDto.getRole());
            roles.add(role);

            UserProfile userProfile = new UserProfile();
            userProfile.setEmail(employeeDto.getEmail());
            userProfile.setPassword(bcryptEncoder.encode(employeeDto.getPassword()));
            userProfile.setRoles(roles);

            employeeEntity.setUserProfile(userProfile);

            return new ResponseEntity<>(employeeRepo.save(employeeEntity), HttpStatus.OK);
        }
    }

    /**
     * create user role
     * @param role
     * @return
     */
    @PostMapping("/addRole")
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        if(roleRepository.findByRole(role.getRole()) != null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(roleRepository.save(role), HttpStatus.OK);
    }
}
