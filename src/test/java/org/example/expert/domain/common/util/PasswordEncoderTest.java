package org.example.expert.domain.common.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

	@InjectMocks
	private PasswordEncoder passwordEncoder;

	@Test
	void matches_메서드가_정상적으로_동작한다() {
		// given
		String rawPassword = "testPassword";
		String encodedPassword = passwordEncoder.encode(rawPassword);

		// when
		boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

		// then
		assertTrue(matches);
	}
}
