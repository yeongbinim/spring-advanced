package org.example.expert.domain.auth.service;

import static org.example.expert.domain.common.exception.ExceptionType.AUTH_FAILED;
import static org.example.expert.domain.common.exception.ExceptionType.EMAIL_DUPLICATE;
import static org.example.expert.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.common.exception.CustomException;
import org.example.expert.domain.common.util.JwtUtil;
import org.example.expert.domain.common.util.PasswordEncoder;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Transactional
	public String signup(SignupRequest signupRequest) {
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			throw new CustomException(EMAIL_DUPLICATE);
		}

		UserRole userRole = UserRole.of(signupRequest.getUserRole());
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
		User savedUser = userRepository.save(new User(
			signupRequest.getEmail(),
			encodedPassword,
			userRole)
		);

		return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);
	}

	public String signin(SigninRequest signinRequest) {
		User user = userRepository.findByEmail(signinRequest.getEmail())
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND));

		if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
			throw new CustomException(AUTH_FAILED);
		}

		return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
	}
}
