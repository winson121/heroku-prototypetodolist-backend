package com.heroku.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.heroku.entity.ToDo;
import com.heroku.entity.User;

@Repository
public class ToDoDAOImpl implements ToDoDAO {
	
	// injecting session factory
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<ToDo> getToDos() {
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// create a query to get ToDoList
		Query<ToDo> query = currentSession.createQuery("from ToDo", ToDo.class);
		
		// execute query and get result list
		List<ToDo> todos = query.getResultList();
		
		// return the results
		return todos;
	}

	@Override
	public void saveToDo(ToDo toDo) {
		
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save/update todos
		currentSession.saveOrUpdate(toDo);

	}

	@Override
	public ToDo getToDo(int id) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// retrieve/read from database using the primary key
		ToDo todo = currentSession.get(ToDo.class, id);
		
		return todo;
	}

	@Override
	public void deleteToDo(int id) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete object with primary key
		Query<?> query = 
				currentSession.createQuery("delete from ToDo where id=:todoId");
		
		query.setParameter("todoId", id);
		
		query.executeUpdate();

	}

	@Override
	public List<ToDo> getToDosByUser(String username) {
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// get the user from username
		Query<User> query = 
				currentSession.createQuery("select u from User u "
								+ "JOIN FETCH u.toDos "
								+ "where u.userName=:uName", User.class);
		
		query.setParameter("uName", username);
		
		List<ToDo> userToDos = null;
		try {
			// get user's todos if todos not null
			User user = query.getSingleResult();
			userToDos = user.getToDos();
		} catch (NoResultException nre) {
			// ignore if todos is empty
		}
		
		return userToDos;
	}
	
	@Override
	public ToDo saveToDoByUser(String username, ToDo toDo) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// querying user with username
		Query<User> query = currentSession.createQuery("from User where userName=:uName", User.class);
		query.setParameter("uName", username);
		
		User user = query.getSingleResult();
		

		// link the toDo with current user_id
		toDo.setUserId(user.getId());
		
		// add todo to the user
		user.addToDo(toDo);
		
		return toDo;
	}
	
	@Override
	public ToDo updateToDoByUser(String username, ToDo toDo) {
		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// query user from database
		Query<User> query = currentSession.createQuery("from User where userName=:uName", User.class);
		query.setParameter("uName", username);
		
		User user = query.getSingleResult();
		
		// set toDo userId to the current logged in user's id
		toDo.setUserId(user.getId());
		
		// save toDo 
		currentSession.saveOrUpdate(toDo);
		
		return toDo;
	}
}
