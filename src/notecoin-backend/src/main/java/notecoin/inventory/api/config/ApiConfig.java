package notecoin.inventory.api.config;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author camille.walim
 * 
 *         Configuration from a network point of view : websocket, routes,
 *         filters, handlers, etc.
 */
@Configuration
class ApiConfig implements WebFluxConfigurer{

	/**
	 * enable swagger for documentation
	 */
	@Bean
	public OpenAPI customOpenAPI(@Autowired(required = false) BuildProperties buildProperties) {
		return new OpenAPI()
			.info(new Info()
				.title("NoteCoin API").version(Optional.ofNullable(buildProperties).map(BuildProperties::getVersion).orElse("test"))
				.description(""
					+ "<img src=\"notecoin_logo.PNG\" alt=\"Girl in a jacket\" width=\"300\" height=\"50\"><br>" 
					+ "API for Inventory Management")
				);
	}
	 
	/**
	 * cors for UI
	 */
	@Bean
	CorsWebFilter cors() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return new CorsWebFilter(source);
	}
	
	/**
	 * @author user H2 use tomcat, not netty -> so you need a dedicated server for this
	 */
	@Component
	public class H2 {

		private Server webServer;

		@EventListener(ContextRefreshedEvent.class)
		public void start() {
			try {
				this.webServer = org.h2.tools.Server
					.createWebServer("-webPort", "8082", "-tcpAllowOthers")
					.start();
			} catch (SQLException e) {
				System.out.print(" h2: port already in use.");
			}
		}

		@EventListener(ContextClosedEvent.class)
		public void stop() { this.webServer.stop(); }

	}

}