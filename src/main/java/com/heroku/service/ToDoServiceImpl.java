package com.heroku.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heroku.dao.ToDoDAO;
import com.heroku.entity.ToDo;

@Service
public class ToDoServiceImpl implements ToDoService {
	
	// inject ToDo DAO
	@Autowired
	private ToDoDAO toDoDAO;
	
	@Override
	@Transactional // this is Advice will begin and commit transaction for us
	public List<ToDo> getToDos() {
		return toDoDAO.getToDos();
	}

	@Override
	@Transactional
	public void saveToDo(ToDo toDo) {
		toDoDAO.saveToDo(toDo);
	}

	@Override
	@Transactional
	public ToDo getToDo(int id) {
		return toDoDAO.getToDo(id);
	}

	@Override
	@Transactional
	public void deleteToDo(int id) {
		
		toDoDAO.deleteToDo(id);
	}

	@Override
	@Transactional
	public List<ToDo> getToDosByUser(String username) {

		return toDoDAO.getToDosByUser(username);
	}

	@Override
	@Transactional
	public ToDo saveToDoByUser(String username, ToDo toDo) {
		return toDoDAO.saveToDoByUser(username, toDo);
	}
	
	@Override
	@Transactional
	public ToDo updateToDoByUser(String username, ToDo toDo) {
		return toDoDAO.updateToDoByUser(username, toDo);
	}
	
	
}
