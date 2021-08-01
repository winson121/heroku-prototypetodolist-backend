package com.heroku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.dto.UserDTO;
import com.heroku.entity.Role;
import com.heroku.entity.User;
import com.heroku.service.RoleService;
import com.heroku.service.UserService;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody UserDTO userDto) {
		String userName = userDto.getUsername();
		
		User existing = userService.findByUserName(userName);
		
		if(existing != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		userService.save(userDto);
		return new ResponseEntity<>(existing, HttpStatus.CREATED);
	}
	
	@GetMapping("/login/{username}")
	public ResponseEntity<?> login(@PathVariable String username) {
		
		User persistentUser = userService.findByUserName(username);

		if (persistentUser != null) {
			User user = new User(persistentUser.getId(), persistentUser.getUserName(),
					persistentUser.getPassword(), persistentUser.getFirstName(),
					persistentUser.getLastName(), persistentUser.getEmail(),
					persistentUser.getRoles());

			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/role/{roleName}")
	public ResponseEntity<?> getRolesByRoleName(@PathVariable String roleName) {
		Role role = roleService.findRoleByName(roleName);
		
		if (role != null) {
			return new ResponseEntity<>(role, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
}
