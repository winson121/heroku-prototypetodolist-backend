package com.heroku.dao;

import com.heroku.entity.User;

public interface UserDAO {
	
	User findByUserName(String userName);
	
	void save(User user);

}
