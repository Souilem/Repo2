package com.dnaproduction.dnproject.controler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.dnaproduction.dnproject.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dnaproduction.dnproject.entity.AppRole;
import com.dnaproduction.dnproject.entity.AppUser;

@RestController
public class AccountRestController {
	
	private AccountService accountService;

	public AccountRestController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping(path= "/users")
	@PostAuthorize("hasAuthority('USER')")
	public List<AppUser> appUsers(){
		return accountService.listUsers();
	}
	
	@PostMapping(path= "/users")
	@PostAuthorize("hasAuthority('ADMIN')")
	public AppUser saveUser(@RequestBody AppUser appUser) {
		return accountService.addNewUser(appUser);	
	}
	
	@PostMapping(path= "/roles")
	@PostAuthorize("hasAuthority('USER')")
	public AppRole saveRole(@RequestBody AppRole appRole) {
		return accountService.addNewRole(appRole);
	}
	
	
	@PostMapping(path= "/addRoleToUser")
	@PostAuthorize("hasAuthority('ADMIN')")
	public void addRoleToUser(@RequestBody RoleUserForm roleUserForm) {
	  accountService.addRoleToUser(roleUserForm.getUsername(), roleUserForm.getRoleName()); 
	}
	
	@GetMapping(path = "/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String authToken = request.getHeader("Authorization");
		if(authToken!= null && authToken.startsWith("Bearer ")) {
			
			try {
				
				String jwt = authToken.substring(7);
				Algorithm algorithm = Algorithm.HMAC256("mysec1234");
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				
				//session de lutilisateur
				String username = decodedJWT.getSubject();
				
				AppUser appUser = accountService.loadUserByUsername(username);
			
				String jwtAccessToken  = JWT.create()
						.withSubject(appUser.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+ 1*60*1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList())).sign(algorithm);
				
				Map <String, String> idToken = new HashMap<>();
				idToken.put("access-token", jwtAccessToken);
				idToken.put("refresh-token", jwt);
				response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
				
			} catch (Exception e) {
				throw e;
			}
	
		} else {
			
			throw new RuntimeException("Refresh token REQUIRED!");
		}
			
		
	}


}

@Data
class RoleUserForm {
	private String username;
	private String roleName;
	
}
