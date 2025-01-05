package org.example.expert.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.example.expert.domain.common.exception.ExceptionType.TODO_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.CustomException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@Mock
	private CommentRepository commentRepository;
	@Mock
	private TodoRepository todoRepository;
	@InjectMocks
	private CommentService commentService;

	@Test
	@DisplayName("saveComment: 예외 - 댓글이 생성될 todo가 없을 때")
	void saveComment_Exception1() {
		// given
		long todoId = 1L;
		CommentSaveRequest request = new CommentSaveRequest("contents");
		AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

		given(todoRepository.findById(todoId)).willReturn(Optional.empty());

		// when
		CustomException exception = catchThrowableOfType(
			() -> commentService.saveComment(authUser, todoId, request),
			CustomException.class
		);

		// then
		assertThat(exception.getCode()).isEqualTo(TODO_NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("saveComment: 정상 등록")
	public void saveComment_Success() {
		// given
		long todoId = 1L;
		CommentSaveRequest request = new CommentSaveRequest("contents");
		AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
		User user = User.fromAuthUser(authUser);
		Todo todo = new Todo("title", "title", "contents", user);
		Comment comment = new Comment(request.getContents(), user, todo);

		given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
		given(commentRepository.save(any())).willReturn(comment);

		// when
		Comment result = commentService.saveComment(authUser, todoId, request);

		// then
		assertThat(result).isNotNull();
	}
}
