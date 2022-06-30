package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Account;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Role;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.RoleRepository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService{
	private AccountRepository accountRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private EmployeeService  employeeService;
	private static final Logger log = (Logger) LoggerFactory.getLogger(UserServiceImpl.class);
		
	public UserServiceImpl(AccountRepository accountRepository, RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,EmployeeService employeeService) {
		super();
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.employeeService=employeeService;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Account user = accountRepository.findAccountByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException("User Not Found In The Database");
		}
		else {
			System.out.println("User Found In The Database");
		}
		
		//SimpleGrantedAuthority(role.getName())
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new  SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
	@Override
	public Account saveUserAccount(Account account) {
		log.info("Saving new user {} in the database", account.getUsername());
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		return accountRepository.save(account);		
	}

	@Override
	public Role saveRole(Role role) {
		
		log.info("Saving new role {} in the database", role.getName());
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		
		log.info("Adding role {} to user {}", roleName,username);
		Account acc= accountRepository.findAccountByUsername(username);
		Role role = roleRepository.findByName(roleName);
		acc.getRoles().add(role);		
	}

	@Override
	public Account getUserAccount(String username) {
		log.info("Fetching user {}",username);
		return accountRepository.findAccountByUsername(username);
	}
	
	@Override
	public List<Account> getUsersAccounts() {
		
		log.info("Fetching all users");
		return accountRepository.findAll();
	}

	@Override
	public Employee getUserInfo(String matricule) {
		return employeeService.findEmployeeByMatricule(matricule);
	}

}
