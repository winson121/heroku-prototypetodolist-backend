package com.heroku.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.heroku.entity.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Role findRoleByName(String roleName) {
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// create query to get role
		Query<Role> query = currentSession.createQuery("from Role where name=:roleName", Role.class);
		query.setParameter("roleName", roleName);
		
		Role role = null;
		
		try {
			role = query.getSingleResult();
		} catch (Exception e){
			
		}
		
		return role;
	}

}
