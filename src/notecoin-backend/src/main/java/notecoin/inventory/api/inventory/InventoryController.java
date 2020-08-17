package notecoin.inventory.api.inventory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductDetailsAbstract;
import notecoin.inventory.domain.service.AbstractInventoryBrowser;
import notecoin.inventory.domain.service.AbstractInventoryBrowser.PositionType;
import notecoin.inventory.domain.service.AbstractInventoryCreator;
import notecoin.inventory.domain.service.AbstractInventoryUpdater;

/**
 * @author camille.walim
 * 
 * Expose a list of services through endpoints to the user.
 */
@RestController
public class InventoryController {
	
	@Autowired
	private AbstractInventoryCreator creator;
	@Autowired
	private AbstractInventoryUpdater updater;
	@Autowired
	private AbstractInventoryBrowser browser;
	

	@PutMapping("/inventory/product/create")
	String create(String product, String category, String subcategory) {
		return illegalArguments(()->{
			creator.create(product, category, subcategory);
			return "Created";
		});
	}
	@PatchMapping("/inventory/product/details")
	Product patch(String name, 
		String origin, Double price, String currency,
		ProductDetailsAbstract details) {
		return illegalArguments(()-> creator.updateDetails(name, origin, price, currency, details));
	}

	@PostMapping("/inventory/instruction")
	String instruction(String product, AbstractInventoryUpdater.OrderType type, int quantity, 
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date when, 
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date until) {
		return illegalArguments(()->{
			updater.instruction(product, type, quantity, when, until);
			return "ok";
		});
	}
	
	@GetMapping("/inventory/position")
	Map<String, Map<Date, Integer>> browse(PositionType type, String product, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date date) {
		return illegalArguments(()-> browser.getPositions(type, product, date));
	}
	
	@GetMapping("/inventory/instructions")
	List<Instruction> browse() {
		return illegalArguments(()-> browser.getAll() );
	}
	
	
	@PostMapping("/inventory/update")
	@Deprecated // Mk-I feature (inventory non-event based)
	String update(String product, int quantity) {
		return illegalArguments(()->{
			updater.update(product, quantity);
			return "Updated";
		});
	}

	
	public <T> T illegalArguments(Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception exc) {
			throw new ResponseStatusException(
					exc instanceof IllegalArgumentException ? HttpStatus.BAD_REQUEST
				:	exc instanceof NoSuchElementException ? HttpStatus.NOT_FOUND
				: 	HttpStatus.INTERNAL_SERVER_ERROR,
				exc.getMessage(), exc);
		}
	}
}
