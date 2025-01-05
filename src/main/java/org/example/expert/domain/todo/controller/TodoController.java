package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<TodoResponse> saveTodo(
		@Auth AuthUser authUser,
		@Valid @RequestBody TodoSaveRequest todoSaveRequest
	) {
		Todo todo = todoService.createTodo(authUser, todoSaveRequest);

		return ResponseEntity
			.ok(TodoResponse.toDto(todo));
	}

	@GetMapping
	public ResponseEntity<Page<TodoResponse>> getTodos(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Page<Todo> todos = todoService.getTodos(page, size);

		return ResponseEntity
			.ok(todos.map(TodoResponse::toDto));
	}

	@GetMapping("/{todoId}")
	public ResponseEntity<TodoResponse> getTodo(@PathVariable long todoId) {
		Todo todo = todoService.getTodo(todoId);

		return ResponseEntity
			.ok(TodoResponse.toDto(todo));
	}
}
