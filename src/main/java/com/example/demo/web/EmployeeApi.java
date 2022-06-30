package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Employee;
import com.example.demo.services.EmployeeService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmployeeApi {

	@Autowired
	EmployeeService employeeService;
	@GetMapping(path = "/employees")
	List<Employee> getAllEmplyees()
	{
		return employeeService.listEmployees();
	}
	@PostMapping(path = "/employee/add")
	Employee addEmplyees(@RequestBody Employee e)
	{
		return employeeService.addEmployee(e);
	}
	@DeleteMapping (path = "/employee/delete/{matricule}")
	 Employee deleteEmplyees(@PathVariable String matricule)
	{
		return employeeService.deleteEmployee(matricule);
	}
}
