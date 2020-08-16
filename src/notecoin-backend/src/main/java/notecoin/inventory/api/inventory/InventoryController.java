package notecoin.inventory.api.inventory;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.service.AbstractInventoryBrowser;
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
	

	@PutMapping("/inventory/create")
	String create(String product, String category, @Nullable String subcategory) {
		return illegalArguments(()->{
			creator.create(product, category, subcategory);
			return "Created";
		});
	}

	@PostMapping("/inventory/update")
	@Deprecated
	String update(String product, int quantity) {
		return illegalArguments(()->{
			updater.update(product, quantity);
			return "Updated";
		});
	}
	

	@GetMapping("/inventory/browse")
	List<Instruction> browse(String product) {
		return illegalArguments(()-> product==null ? browser.getAll() : Collections.singletonList(browser.get(product)));
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
