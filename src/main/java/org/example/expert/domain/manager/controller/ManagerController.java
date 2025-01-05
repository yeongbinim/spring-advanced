package org.example.expert.domain.manager.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos/{todoId}/managers")
public class ManagerController {

	private final ManagerService managerService;

	@PostMapping
	public ResponseEntity<ManagerResponse> saveManager(
		@Auth AuthUser authUser,
		@PathVariable long todoId,
		@Valid @RequestBody ManagerSaveRequest managerSaveRequest
	) {
		Manager manager = managerService.saveManager(authUser, todoId, managerSaveRequest);

		return ResponseEntity
			.ok(ManagerResponse.toDto(manager));
	}

	@GetMapping
	public ResponseEntity<List<ManagerResponse>> getMembers(@PathVariable long todoId) {
		List<Manager> managerList = managerService.getManagers(todoId);

		return ResponseEntity
			.ok(managerList.stream().map(ManagerResponse::toDto).toList());
	}

	@DeleteMapping("/{managerId}")
	public ResponseEntity<Void> deleteManager(
		@Auth AuthUser authUser,
		@PathVariable long todoId,
		@PathVariable long managerId
	) {
		managerService.deleteManager(authUser, todoId, managerId);

		return ResponseEntity.noContent().build();
	}
}
