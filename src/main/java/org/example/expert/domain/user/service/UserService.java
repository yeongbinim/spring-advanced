package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.util.PasswordEncoder;
import org.example.expert.domain.user.dto.request.UserPasswordChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User getUser(long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new InvalidRequestException("User not found"));
	}

	@Transactional
	public void changePassword(long userId, UserPasswordChangeRequest userPasswordChangeRequest) {
		User user = getUser(userId);

		if (passwordEncoder.matches(userPasswordChangeRequest.getNewPassword(),
			user.getPassword())) {
			throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
		}

		if (!passwordEncoder.matches(userPasswordChangeRequest.getOldPassword(),
			user.getPassword())) {
			throw new InvalidRequestException("기존 비밀번호가 일치하지 않습니다.");
		}
		
		user.changePassword(passwordEncoder.encode(userPasswordChangeRequest.getNewPassword()));
	}
}
