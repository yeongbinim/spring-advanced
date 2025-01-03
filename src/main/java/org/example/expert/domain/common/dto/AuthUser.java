package org.example.expert.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@AllArgsConstructor
public class AuthUser {

	private final Long id;
	private final String email;
	private final UserRole userRole;
}
