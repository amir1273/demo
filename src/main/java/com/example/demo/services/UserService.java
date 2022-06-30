package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Account;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Role;

public interface UserService {
	
	Account saveUserAccount(Account account);
	Role saveRole(Role role);
	void addRoleToUser(String username, String roleName);
	Account getUserAccount(String username);
	List<Account> getUsersAccounts();
	 Employee getUserInfo(String username);
}
