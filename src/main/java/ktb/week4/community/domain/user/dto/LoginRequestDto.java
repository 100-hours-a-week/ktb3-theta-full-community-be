package ktb.week4.community.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
		@Schema(description = "이메일", example = "demo@email.com")
		@NotBlank(message = "이메일은 필수입니다.")
		String email,
		
		@Schema(description = "비밀번호", example = "password1234")
		@NotBlank(message = "비밀번호는 필수입니다.")
		String password
) {
}
