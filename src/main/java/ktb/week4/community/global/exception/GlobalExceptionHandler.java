package ktb.week4.community.global.exception;

import io.swagger.v3.oas.annotations.Hidden;
import ktb.week4.community.global.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
		return ApiResponse.onFailure("400", e.getMessage());
	}
	
	@ExceptionHandler(GeneralException.class)
	public ApiResponse<Void> handleUnauthorizedException(GeneralException e) {
		return ApiResponse.onFailure(e.getErrorCode().getReason().code(), e.getErrorCode().getReason().message());
	}
}
