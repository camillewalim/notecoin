package notecoin.inventory.domain.service;

import notecoin.inventory.domain.model.InventoryQuantity;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryUpdater{
	
	InventoryQuantity update(String name, int quantity);

}
