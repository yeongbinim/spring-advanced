package org.example.expert.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {

	private final CommentAdminService commentAdminService;

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
		commentAdminService.deleteComment(commentId);

		return ResponseEntity.noContent().build();
	}
}
