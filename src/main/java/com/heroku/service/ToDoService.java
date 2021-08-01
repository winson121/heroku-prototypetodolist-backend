package com.heroku.service;

import java.util.List;

import com.heroku.entity.ToDo;

public interface ToDoService {
	
	public List<ToDo> getToDos();
	
	public void saveToDo(ToDo toDo);
	
	public ToDo getToDo(int id);
	
	public void deleteToDo(int id);
	
	public List<ToDo> getToDosByUser(String username);
	
	public ToDo saveToDoByUser(String username, ToDo toDo);
	
	public ToDo updateToDoByUser(String username, ToDo toDo);
}
