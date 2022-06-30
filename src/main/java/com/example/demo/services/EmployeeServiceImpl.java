package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository employeeRepo;
	@Override
	public Employee addEmployee(Employee e) {
		return employeeRepo.save(e);
	}

	@Override
	public Employee deleteEmployee(String matricule) {
			Employee e = employeeRepo.findEmployeeByMatricule(matricule);
			 employeeRepo.delete(e);
			 return e;
	}

	@Override
	public List<Employee> listEmployees() {
		return employeeRepo.findAll();
	}

	@Override
	public Employee findEmployeeByMatricule(String matricule) {
		// TODO Auto-generated method stub
		return employeeRepo.findEmployeeByMatricule(matricule);
	}

}
