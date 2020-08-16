package notecoin.inventory.domain.service;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.model.InventoryQuantity;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryUpdater implements AbstractInventoryUpdater{
	
	private JpaRepository<InventoryName, String> nameDao;
	private JpaRepository<InventoryQuantity, Integer> quantityDao;
	
	@Override
	public InventoryQuantity update(String name, int quantity) {
		throw new RuntimeException("not implemented"); 
	}
	
}
