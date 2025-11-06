package ktb.week4.community.global.exception;


import org.springframework.http.HttpStatus;

public record Reason(
		HttpStatus httpStatus,
		String code,
		String message
) {
}
