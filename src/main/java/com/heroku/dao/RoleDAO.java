package com.heroku.dao;

import com.heroku.entity.Role;

public interface RoleDAO {
	
	public Role findRoleByName(String roleName);
}
