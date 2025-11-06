package ktb.week4.community.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserRequestDto(
		@Schema(description = "사용자 별명", example = "다이빙 하는 돌고래")
		String nickname,
		
		@Schema(description = "사용자의 프로필 이미지", example = "사용자의 프로필 이미지가 따로 설정되어 있다면, url을 첨부해주세요.")
		@JsonProperty("profile_image")
		String profileImage
) {
}
