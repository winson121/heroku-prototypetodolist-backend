package com.heroku.service;

import com.heroku.entity.Role;

public interface RoleService {
	
	public Role findRoleByName(String roleName);
}
