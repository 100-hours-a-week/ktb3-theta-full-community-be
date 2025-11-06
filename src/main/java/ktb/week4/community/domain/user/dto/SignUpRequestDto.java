package ktb.week4.community.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequestDto(
		
		@Schema(description = "이메일", example = "demo@email.com")
		@NotNull(message = "이메일은 필수입니다.")
		@Email
		String email,
		
		@Schema(description = "비밀번호", example = "password1234")
		@NotBlank(message = "비밀번호는 필수입니다.")
		String password,
		
		@Schema(description = "사용자 별명", example = "다이빙 하는 돌고래")
		@NotBlank(message = "사용자 별명은 필수입니다.")
		String nickname,
		
		@Schema(description = "사용자의 프로필 이미지", example = "사용자의 프로필 이미지가 따로 설정되어 있다면, url을 첨부해주세요.")
		String profileImage
) {
}
