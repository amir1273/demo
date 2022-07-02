package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


import com.example.demo.entities.Account;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Fonction;
import com.example.demo.entities.Gender;
import com.example.demo.entities.Role;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.UserService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//            	registry.addMapping("/api/login").allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
//                .allowedOrigins("http://localhost:3000")
//                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin")
//            .exposedHeaders("Access-Control-Expose-Headers", "Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Origin");            }
//        };
//    }
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		
//		List<Role> roles = roleRepository.findAll();
//
//		Account account = new Account(null, "admin","admin", roles);
//		userService.saveUserAccount(account);
//		Employee employee= new Employee(null, "0456", "04819664", "adminadmin", new Date(), new Date(), "0123456", 20.5, "123456789", Gender.Homme, Fonction.TechnicienChef);
//		employeeRepository.save(employee);
	}
		
}
