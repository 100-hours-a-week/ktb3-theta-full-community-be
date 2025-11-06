package ktb.week4.community.global.exception;

import ktb.week4.community.global.apiPayload.BaseErrorCode;

public class GeneralException extends RuntimeException {
	
	private final BaseErrorCode code;
	
	public GeneralException(BaseErrorCode code) {
		super(code.getReason().message());
		this.code = code;
	}
	
	public BaseErrorCode getErrorCode() {
		return code;
	}
}
