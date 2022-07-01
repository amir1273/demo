package com.example.demo.web;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.demo.entities.Account;
import com.example.demo.entities.Employee;
import com.example.demo.entities.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(path="/api")
public class UserAccountApi {
	
	private static final String APPLICATION_JSON_VALUE = "application/json";
	private UserService userService;

	public UserAccountApi(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	EmployeeService employeeService;
	@Autowired
	private RoleRepository roleRepository;
	@GetMapping(path = "/employees")
	List<Employee> getAllEmplyees()
	{
		return employeeService.listEmployees();
	}
	@PostMapping(path = "/employee/add")
	Employee addEmplyees(@RequestBody Employee e)
	
	{
		Collection<Role> roles = new ArrayList<Role>();
		roles.add(roleRepository.findRoleByName("USER"));
		Account a = new Account(null, e.getMatricule(), e.getCin(), roles);
		userService.saveUserAccount(a);
		e.setAccount(a);
		return employeeService.addEmployee(e);
	}
	
	@DeleteMapping (path = "/employee/delete/{matricule}")
	 Employee deleteEmplyees(@PathVariable String matricule)
	{
		return employeeService.deleteEmployee(matricule);
	}
	
	@GetMapping(path="/user/info/{matricule}")
	public Employee getUserInfo( @PathVariable String matricule) {
		
		return userService.getUserInfo( matricule);
	}
	@GetMapping(path="/users")
	public ResponseEntity<List<Account>>getUsers() {
		
		return ResponseEntity.ok().body(userService.getUsersAccounts());
		
	}
	
	
	@PostMapping(path="/user/save")
	public ResponseEntity<Account> saveUser(@RequestBody Account account) {
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		System.out.println(account.getUsername());
		return ResponseEntity.created(uri).body(userService.saveUserAccount(account));
		
	}
	@PostMapping(path="/role/save")
	public ResponseEntity<Role> saveUser(@RequestBody Role role) {
		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		
		return ResponseEntity.created(uri).body(userService.saveRole(role));
		
	}
	@PostMapping(path="/role/addtouser")
	public ResponseEntity<Account> addRoleToUser(@RequestBody RoleToUserForm form) {
		
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		
		return ResponseEntity.ok().build();
		
	}
	@GetMapping(path="/token/refresh")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {
		
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			
			try {
				
				String token = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(token);
				String username = decodedJWT.getSubject();
				Account user = userService.getUserAccount(username);
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+10 *60 *1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				
				String refresh_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis()+30 *60 *1000))
						.withIssuer(request.getRequestURL().toString())
						.sign(algorithm);
				
				
				Map<String, String> tokens = new HashMap<>();
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
								
			} catch(Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				//response.sendError(FORBIDDEN.value());
				
				Map<String, String> error = new HashMap<>();
				error.put("error_message", exception.getMessage());
				
				response.setContentType(APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}	
							
		} else {
			throw new RuntimeException("refresh token is missing");
		}
		
	}
}
class RoleToUserForm {
	private String username;
	private String roleName;
	public String getUsername() {
		return username;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
}