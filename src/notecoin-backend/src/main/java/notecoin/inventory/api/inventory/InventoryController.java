package notecoin.inventory.api.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

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
		creator.create(name, category, subcategory);
		return "Created";
	}

	@PostMapping("/inventory/update")
	String update(String name, int quantity) {
		updater.update(name, quantity);
		return "Updated";
	}
	
}
