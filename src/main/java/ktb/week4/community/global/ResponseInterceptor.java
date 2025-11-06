package ktb.week4.community.global;

import io.swagger.v3.oas.annotations.Hidden;
import ktb.week4.community.global.apiPayload.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Hidden
@RestControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice {
	
	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if(returnType.getParameterType() == ApiResponse.class) {
			String code = ((ApiResponse<?>)body).getCode();
			HttpStatus status = HttpStatus.valueOf((Integer.parseInt(code.substring(code.length() - 3))));
			response.setStatusCode(status);
		}
		return body;
	}
}
