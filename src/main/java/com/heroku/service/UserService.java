package com.heroku.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.heroku.dto.UserDTO;
import com.heroku.entity.User;

public interface UserService extends UserDetailsService{
	
	User findByUserName(String userName);

	void save(UserDTO userDto);

}
