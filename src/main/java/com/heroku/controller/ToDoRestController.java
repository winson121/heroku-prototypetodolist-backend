package com.heroku.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heroku.entity.ToDo;
import com.heroku.entity.User;
import com.heroku.exception.ToDoNotFoundException;
import com.heroku.service.ToDoService;
import com.heroku.service.UserService;

@RestController
@RequestMapping("/api")
public class ToDoRestController {
	
	// autowire the ToDoService
	@Autowired
	private ToDoService toDoService;
	
	// autowire the UserService
	@Autowired
	private UserService userService;
	
	// add mapping to GET /todos
	@GetMapping("/todos")
	public ResponseEntity<List<ToDo>> getToDos(@RequestHeader Map<String, String> header) {
		
		List<ToDo> toDos = toDoService.getToDos();
		return new ResponseEntity<>(toDos, HttpStatus.OK);
	}
	
	// add mapping for GET /todos/{todoId}
	
	@GetMapping("/todos/{toDoId}")
	public ResponseEntity<ToDo> getToDo(@PathVariable int toDoId) {
		
		ToDo toDo = toDoService.getToDo(toDoId);
		
		if (toDo == null) {
			throw new ToDoNotFoundException("ToDo id not found - " + toDoId);
		}
		
		return new ResponseEntity<>(toDo, HttpStatus.OK);
	}
	
	// add mapping for POST /todos/users - add new ToDo
	
	@PostMapping("/todos/users")
	public ResponseEntity<ToDo> saveToDoByUser(@RequestBody ToDo toDo, Principal principal) {
		
		// just in case an id is pass in the JSON, we will set the id to 0
		// this will force a save of new item instead of updating the todo
		if (toDo == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		toDo.setId(0);
		
		// assign the activity to the user that is currently logged in
		toDoService.saveToDoByUser(principal.getName(), toDo);
		
		return new ResponseEntity<>(toDo, HttpStatus.OK);
	}
	
	@PutMapping("/todos/users")
	public ResponseEntity<ToDo> updateToDoByUser(@RequestBody ToDo toDo, Principal principal) {
		if (toDo == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		toDoService.updateToDoByUser(principal.getName(), toDo);
		
		return new ResponseEntity<>(toDo, HttpStatus.OK);
	}
	
	// add mapping for DELETE /todos/ - delete todo 
	@DeleteMapping("/todos/users/{toDoId}")
	public ResponseEntity<String> deleteToDoByUser(@PathVariable int toDoId, Principal principal) {
		
		ToDo toDo = toDoService.getToDo(toDoId);
		
		User authenticatedUser = userService.findByUserName(principal.getName());
		if ((toDo == null) || (toDo.getUserId() != authenticatedUser.getId())) {
			throw new ToDoNotFoundException("ToDo id not found or Authenticated user don't have access to this ToDo - " + toDoId);
		}
		
		toDoService.deleteToDo(toDoId);
		
		return new ResponseEntity<>("Deleted todo id - " + toDoId, HttpStatus.OK);
	}
	
	@GetMapping("/todos/users")
	public ResponseEntity<List<ToDo>> getToDosByUser(Principal principal) {
		
		List<ToDo> userToDos = toDoService.getToDosByUser(principal.getName());
		
		if (userToDos != null) {
			return new ResponseEntity<>(userToDos, HttpStatus.OK);
		}
		
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
}
