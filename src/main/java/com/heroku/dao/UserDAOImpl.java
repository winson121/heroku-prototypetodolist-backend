package com.heroku.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.heroku.entity.User;

@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public User findByUserName(String userName) {
		
		// get current session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// create query to get the user
		Query<User> query = currentSession.createQuery("select u from User u"
				+ " JOIN FETCH u.roles where userName=:userName", User.class);
		query.setParameter("userName", userName);
		
		// check if the user with the username exist in the db
		User user = null;
		
		try {
			user = query.getSingleResult();
			System.out.println(user);
		} catch (Exception e) {
		}
		
		return user;
	}

	@Override
	public void save(User user) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		currentSession.saveOrUpdate(user);

	}

}
