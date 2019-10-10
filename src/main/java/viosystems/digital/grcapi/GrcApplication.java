package viosystems.digital.grcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class GrcApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrcApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> initCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "clientHeaders"));
		config.addAllowedMethod("*");
		config.setAllowedOrigins(Collections.singletonList("*"));
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
