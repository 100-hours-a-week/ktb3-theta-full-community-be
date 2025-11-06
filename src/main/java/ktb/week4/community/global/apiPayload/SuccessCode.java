package ktb.week4.community.global.apiPayload;

import ktb.week4.community.global.exception.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode implements BaseSuccessCode{
	_OK(HttpStatus.OK, "COMMON200", "success"),
	_CREATED(HttpStatus.CREATED, "COMMON201", "create_success"),
	
	SUCCESS(HttpStatus.OK, "200", "success"),
	CREATE_SUCCESS(HttpStatus.CREATED, "201", "create_success"),
	UPDATE_SUCCESS(HttpStatus.OK, "200", "update_success"),
	;
	
	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
	
	@Override
	public Reason getReason() {
		return new Reason(this.httpStatus, this.code, this.message);
	}
}
