package notecoin.inventory.api.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import notecoin.inventory.domain.service.AbstractInventoryCreator;

/**
 * @author camille.walim
 * 
 * Expose a list of services through endpoints to the user.
 */
@RestController
public class InventoryController {
	
	@Autowired
	private AbstractInventoryCreator creator;


	@GetMapping("/inventory/create")
	String create(String name, String category, String subcategory) {
		creator.create(name, category, subcategory);
		return "Created";
	}
}
