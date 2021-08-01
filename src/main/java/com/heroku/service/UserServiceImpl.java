package com.heroku.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heroku.dao.RoleDAO;
import com.heroku.dao.UserDAO;
import com.heroku.dto.UserDTO;
import com.heroku.entity.Role;
import com.heroku.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	// inject UserDAO and RoleDAO
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Override
	@Transactional
	public User findByUserName(String userName) {
		return userDAO.findByUserName(userName);
	}

	@Override
	@Transactional
	public void save(UserDTO userDto) {
		
		// save user dto information to user object
		User user = new User();
		user.setUserName(userDto.getUsername());
		user.setPassword(userDto.getPassword());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		
		// give user default role
		user.setRoles(Arrays.asList(roleDAO.findRoleByName("ROLE_Default")));
		
		// save user to the database
		userDAO.save(user);
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUserName(username);
		System.out.println(user);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid Username or Password");
		}
		
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

}
