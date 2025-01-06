package org.example.expert.domain.common.client;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import org.example.expert.domain.common.client.dto.WeatherDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherClient {

	private final RestTemplate restTemplate;

	public WeatherClient(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public String getTodayWeather() {
		ResponseEntity<WeatherDto[]> responseEntity =
			restTemplate.getForEntity(buildWeatherApiUri(), WeatherDto[].class);
		if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
			throw new RuntimeException(
				"날씨 데이터를 가져오는데 실패했습니다. 상태 코드: " + responseEntity.getStatusCode());
		}

		Optional<WeatherDto[]> optionalWeather = Optional.ofNullable(responseEntity.getBody());
		WeatherDto[] weatherArray = optionalWeather.orElseThrow(
			() -> new RuntimeException("날씨 데이터가 없습니다."));

		String today = getCurrentDate();

		return Arrays.stream(weatherArray)
			.filter(weather -> today.equals(weather.getDate()))
			.findFirst()
			.map(WeatherDto::getWeather)
			.orElseThrow(() -> new RuntimeException("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다."));
	}

	private URI buildWeatherApiUri() {
		return UriComponentsBuilder
			.fromUriString("https://f-api.github.io")
			.path("/f-api/weather.json")
			.encode()
			.build()
			.toUri();
	}

	private String getCurrentDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
		return LocalDate.now().format(formatter);
	}
}
