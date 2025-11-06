package ktb.week4.community.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDto(
		
		@JsonProperty("user_id")
		Long userId,
		
		@JsonProperty("profile_image")
		String profileImage
) {}