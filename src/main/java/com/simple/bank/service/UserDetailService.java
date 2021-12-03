package com.simple.bank.service;

import com.simple.bank.entity.UserProfile;
import com.simple.bank.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserProfile userProfile = userProfileRepository.findByEmail(username);
        if(userProfile == null){
            throw new UsernameNotFoundException("User not found with given username");
        }
        return UserDetailsImpl.build(userProfile);
    }
}
