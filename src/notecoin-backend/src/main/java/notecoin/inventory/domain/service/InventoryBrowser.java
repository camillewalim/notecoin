package notecoin.inventory.domain.service;

import java.util.List;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryBrowser implements AbstractInventoryBrowser{
	
	private InventoryQuantityRepository quantityDao;

	@Override
	public List<InventoryQuantity> getAll() {
		throw new RuntimeException("not implemented");
	}

	@Override
	public InventoryQuantity get(String name) {
		throw new RuntimeException("not implemented");
	}
	
	
}
