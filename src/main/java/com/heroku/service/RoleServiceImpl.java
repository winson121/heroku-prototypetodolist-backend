package com.heroku.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heroku.dao.RoleDAO;
import com.heroku.entity.Role;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDAO roleDAO;
	
	@Override
	@Transactional
	public Role findRoleByName(String roleName) {
		return roleDAO.findRoleByName(roleName);
	}

}
