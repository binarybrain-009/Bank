package com.simple.bank.repository;

import com.simple.bank.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    UserProfile findByEmail(String email);
}
