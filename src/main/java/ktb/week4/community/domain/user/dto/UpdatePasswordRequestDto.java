package ktb.week4.community.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequestDto(
		@Schema(description = "비밀번호", example = "password1234")
		@NotBlank(message = "새 비밀번호는 필수입니다.")
		String password
) {
}
