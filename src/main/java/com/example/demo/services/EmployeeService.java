package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Employee;

public interface EmployeeService {
	Employee findEmployeeByMatricule(String matricule);
	Employee addEmployee(Employee e);
	Employee deleteEmployee(String matricule);
	List<Employee> listEmployees();
	

}
