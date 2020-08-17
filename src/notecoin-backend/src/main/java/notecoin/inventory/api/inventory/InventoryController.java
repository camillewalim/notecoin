package notecoin.inventory.api.inventory;

import static notecoin.inventory.api.inventory.InventoryController.Routes.browseInstruction;
import static notecoin.inventory.api.inventory.InventoryController.Routes.browsePosition;
import static notecoin.inventory.api.inventory.InventoryController.Routes.create;
import static notecoin.inventory.api.inventory.InventoryController.Routes.instruction;
import static notecoin.inventory.api.inventory.InventoryController.Routes.inventory;
import static notecoin.inventory.api.inventory.InventoryController.Routes.patch;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductDetailsAbstract;
import notecoin.inventory.domain.service.AbstractInventoryBrowser;
import notecoin.inventory.domain.service.AbstractInventoryBrowser.PositionType;
import notecoin.inventory.domain.service.AbstractInventoryCreator;
import notecoin.inventory.domain.service.AbstractInventoryUpdater;
import notecoin.inventory.domain.service.AbstractInventoryUpdater.OrderType;
import notecoin.utils.NonVerbose;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author camille.walim
 * 
 * Expose a list of services through endpoints to the user.
 */
@Configuration
public class InventoryController {
	
	public static class Routes {
		static final String 
			inventory = "/inventory",
			create = inventory+			"/product/create",
			patch = inventory+			"/product/details",
			instruction = 	inventory+	"/instruction",
			browseInstruction = 	inventory+	"/instructions",
			browsePosition = inventory+	"/position";
	}
	
	/**
	 * Swagger Group
	 */
	@Bean
	public GroupedOpenApi employeesOpenApi() {
		return GroupedOpenApi.builder().group("inventory").pathsToMatch(inventory+"/**" ).build();
	}
	
	/**
	 * routes for http/s protocols
	 */
	@Bean
	@RouterOperations({
		@RouterOperation(path = create, 			beanClass = AbstractInventoryCreator.class, beanMethod = "create"),
		@RouterOperation(path = patch, 				beanClass = AbstractInventoryCreator.class, beanMethod = "updateDetails"),
		@RouterOperation(path = instruction, 		beanClass = AbstractInventoryUpdater.class, beanMethod = "instruction", 
			operation = @Operation(operationId = "0",parameters = { 
				@Parameter(name = "type", required= true, in = ParameterIn.QUERY, schema= @Schema(implementation=OrderType.class))}) ),
		@RouterOperation(path = browsePosition, 	beanClass = AbstractInventoryBrowser.class, beanMethod = "getPositions"),
		@RouterOperation(path = browseInstruction, 	beanClass = AbstractInventoryBrowser.class, beanMethod = "getAll")
	})
	RouterFunction<ServerResponse> routes(
		AbstractInventoryCreator creator,
		AbstractInventoryUpdater updater,
		AbstractInventoryBrowser browser,
		ObjectMapper mapper) {
		return 		
				route(PUT(create), returnMono(http -> http
					.bodyToMono(asMap)
					.map(body -> creator.create(
						body.get("name"), 
						body.get("category"), 
						body.get("subcategory")))
					,Product.class))
					
			.and(
				route(PATCH(patch),returnMono(http-> http
					.bodyToMono(asMap)
					.map(body -> creator.updateDetails(
						body.get("name"), 
						body.get("origin"), 
						Optional.ofNullable(body.get("price")).map(Double::valueOf).orElse(null), 
						body.get("currency"),
						Optional.ofNullable(body.get("details"))
								.map(NonVerbose	.uncheckedF((String json) -> mapper.reader().readValue(json, ProductDetailsAbstract.class)))
								.orElse(null)))
					,Product.class)))

			.and(
				route(POST(instruction), returnMono(http-> http
					.bodyToMono(asMap)
					.map(body -> updater.instruction(
						body.get("name"), 
						OrderType.valueOf(http.queryParam("type").get()),
						Integer.valueOf(body.get("quantity")),
						parseDate.apply(body.get("date")),
						Optional.ofNullable(body.get("maturity")).map(parseDate).orElse(null)))
					,Instruction.class)))

			.and(
				route(GET(browsePosition), returnMono(http -> Mono
					.just(browser.getPositions(
						PositionType.valueOf(http.queryParam("type").get()), 
						http.queryParam("name").orElse(null), 
						http.queryParam("date").map(parseDate).orElse(null)	))
					,Map.class)))
			
			.and(
				route(GET(browseInstruction), http-> ServerResponse.ok().body(Flux
					.fromIterable(browser.getAll())
					,Product.class)))
			
			;
	}

	
	
//UTILS ***********************************************************
	
	StdDateFormat df = new StdDateFormat();
	Function<String,Date> parseDate = NonVerbose.uncheckedF(df::parse);
	ParameterizedTypeReference<Map<String,String>> asMap = new ParameterizedTypeReference<Map<String,String>>(){};
	
	public HandlerFunction<ServerResponse> returnMono(Function<ServerRequest,Mono<Object>> pipe, Class<?> returnType) {
		return http -> pipe.apply(http)
			.flatMap(data -> ServerResponse.ok().body(Mono.just(data),returnType))
			.onErrorResume(exc ->(
					exc instanceof IllegalArgumentException ?	ServerResponse.status(HttpStatus.BAD_REQUEST)
				:	exc instanceof NoSuchElementException ? 	ServerResponse.status(HttpStatus.NOT_FOUND)
				: 												ServerResponse.status(500))
			.bodyValue(exc.getMessage()));
	}
}
