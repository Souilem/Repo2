package com.dnaproduction.dnproject.services;

import java.util.List;

import com.dnaproduction.dnproject.entity.AppRole;
import com.dnaproduction.dnproject.entity.AppUser;

public interface AccountService {
	
	
	AppUser addNewUser(AppUser appUser);
	AppRole addNewRole(AppRole appRole);
	void addRoleToUser(String username, String roleName);
	AppUser loadUserByUsername(String username);
	List<AppUser> listUsers();
	
	

}
