package org.example.expert.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.AuthResponse;
import org.example.expert.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest signupRequest) {
		String token = authService.signup(signupRequest);
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody SigninRequest signinRequest) {
		String token = authService.signin(signinRequest);
		return ResponseEntity.ok(new AuthResponse(token));
	}
}
