package com.dnaproduction.dnproject.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dnaproduction.dnproject.entity.AppRole;
import com.dnaproduction.dnproject.entity.AppUser;
import com.dnaproduction.dnproject.repository.AppRoleRepository;
import com.dnaproduction.dnproject.repository.AppUserRepository;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	private AppUserRepository appUserRepository;
	private AppRoleRepository appRoleRepository;
	
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
		this.appUserRepository = appUserRepository;
		this.appRoleRepository = appRoleRepository;
	}

	@Override
	public AppUser addNewUser(AppUser appUser) {
		// TODO Auto-generated method stub
		
		String pwd = appUser.getPassword();
		appUser.setPassword(passwordEncoder.encode(pwd));
		return  appUserRepository.save(appUser);
	}

	@Override
	public AppRole addNewRole(AppRole appRole) {
		// TODO Auto-generated method stub
		return appRoleRepository.save(appRole);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		// TODO Auto-generated method stub
		AppUser appUser = appUserRepository.findByUsername(username);
		AppRole appRole = appRoleRepository.findByRoleName(roleName);
		appUser.getAppRoles().add(appRole);	
	}
	
	@Override
	public AppUser loadUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public List<AppUser> listUsers() {
		// TODO Auto-generated method stub
		return appUserRepository.findAll();
	}
}
