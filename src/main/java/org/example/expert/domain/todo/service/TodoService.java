package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

	private final TodoRepository todoRepository;
	private final WeatherClient weatherClient;

	@Transactional
	public Todo createTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
		User user = User.fromAuthUser(authUser);

		String weather = weatherClient.getTodayWeather();

		return todoRepository.save(new Todo(
			todoSaveRequest.getTitle(),
			todoSaveRequest.getContents(),
			weather,
			user
		));
	}

	public Page<Todo> getTodos(int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);

		return todoRepository.findAllByOrderByModifiedAtDesc(pageable);
	}

	public Todo getTodo(long todoId) {
		return todoRepository.findById(todoId)
			.orElseThrow(() -> new InvalidRequestException("Todo not found"));
	}
}
