package com.dnaproduction.dnproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dnaproduction.dnproject.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	
	AppUser findByUsername(String username);

}
