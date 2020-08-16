package notecoin.inventory.api.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



/**
 * @author camille.walim
 * 
 *         Configuration from a network point of view : websocket, routes,
 *         filters, handlers, etc.
 */
@Configuration
@EnableSwagger2
class ApiConfig {

	/**
	 * enable swagger for documentation
	 */
	@Bean
	public Docket swagger() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(new ApiInfoBuilder()
				.title("NoteCoin API").version("0.0.1-SNAPSHOT")
				.description(""
					+ "<img src=\"notecoin_logo.PNG\" alt=\"Girl in a jacket\" width=\"300\" height=\"50\"><br>" 
					+ "API for Inventory Management")
				.build())
			.select().apis(RequestHandlerSelectors.basePackage("notecoin"))
			.paths(PathSelectors.any()).build();

	}
	 
	/**
	 * cors for UI
	 */
	@Bean
	CorsConfiguration cors() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		return configuration;
	}

}