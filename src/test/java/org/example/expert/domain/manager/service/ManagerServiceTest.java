package org.example.expert.domain.manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.example.expert.domain.common.exception.ExceptionType.TODO_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.CustomException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

	@Mock
	private ManagerRepository managerRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private TodoRepository todoRepository;
	@InjectMocks
	private ManagerService managerService;

	@Test
	@DisplayName("getManagers: 예외 - 관리를 생성할 todo가 없을 때")
	public void getMangers_Exception1() {
		// given
		long todoId = 1L;
		given(todoRepository.existsById(todoId)).willReturn(false);

		// when
		CustomException exception = catchThrowableOfType(
			() -> managerService.getManagers(todoId),
			CustomException.class
		);

		// then
		assertThat(exception.getCode()).isEqualTo(TODO_NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("getManagers: 정상 조회")
	public void getMangers_Success() {
		// given
		long todoId = 1L;
		User user = new User("user1@example.com", "password", UserRole.USER);
		Todo todo = new Todo("Title", "Contents", "Sunny", user);
		ReflectionTestUtils.setField(todo, "id", todoId);

		Manager mockManager = new Manager(todo.getUser(), todo);
		List<Manager> mockManagerList = List.of(mockManager);

		given(todoRepository.existsById(todoId)).willReturn(true);
		given(managerRepository.findAllByTodoId(todoId)).willReturn(mockManagerList);

		// when
		List<Manager> result = managerService.getManagers(todoId);

		// then
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getUser().getEmail()).isEqualTo(user.getEmail());
		assertThat(result.get(0).getId()).isEqualTo(mockManager.getId());
	}

	@Test
	@DisplayName("saveManager: 정상 등록")
	void saveManager_Success() {
		// given
		AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
		User user = User.fromAuthUser(authUser);  // 일정을 만든 유저

		long todoId = 1L;
		Todo todo = new Todo("Test Title", "Test Contents", "Sunny", user);

		long managerUserId = 2L;
		User managerUser = new User("b@b.com", "password", UserRole.USER);  // 매니저로 등록할 유저
		ReflectionTestUtils.setField(managerUser, "id", managerUserId);

		// request dto 생성
		ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);

		given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
		given(userRepository.findById(managerUserId)).willReturn(Optional.of(managerUser));
		given(managerRepository.save(any(Manager.class))).willAnswer(
			invocation -> invocation.getArgument(0));

		// when
		Manager result = managerService.saveManager(authUser, todoId, managerSaveRequest);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUser().getId()).isEqualTo(managerUser.getId());
	}
}
