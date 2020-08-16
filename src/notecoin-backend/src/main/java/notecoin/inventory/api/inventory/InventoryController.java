package notecoin.inventory.api.inventory;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

	@PutMapping("/inventory/create")
	String create(String name, String category, String subcategory) {
		return illegalArguments(()->{
			creator.create(name, category, subcategory);
			return "Created";
		});
	}

	@PostMapping("/inventory/update")
	String update(String name, int quantity) {
		return illegalArguments(()->{
			updater.update(name, quantity);
			return "Updated";
		});
	}
	
	public <T> T illegalArguments(Supplier<T> supplier) {
		try {
	        return supplier.get();
	     }
	    catch (IllegalArgumentException exc) {
	         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage(), exc);
	    }
	}
}
