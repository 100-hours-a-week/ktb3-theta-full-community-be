package ktb.week4.community.global.config;

import ktb.week4.community.global.resolver.AuthUserArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final AuthUserArgumentResolver authUserArgumentResolver;
	private final String uploadDir;

	public WebMvcConfig(AuthUserArgumentResolver authUserArgumentResolver, @Value("${file.upload-dir}") String uploadDir) {
		this.authUserArgumentResolver = authUserArgumentResolver;
		this.uploadDir = uploadDir;
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserArgumentResolver);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations(uploadPath);
	}
}