package notecoin.inventory.domain.service;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryUpdater implements AbstractInventoryUpdater{
	
	private InventoryQuantityRepository quantityDao;
	
	@Override
	public InventoryQuantity update(String name, int quantity) {
		return quantityDao
			.findByName(new InventoryName(name,null))
			.map(iq -> {
				iq.setQuantity(quantity);
				return iq;
			})
			.orElseThrow(() -> new IllegalArgumentException("Such inventory name do not exist. Please create it first."));
	}
	
}
