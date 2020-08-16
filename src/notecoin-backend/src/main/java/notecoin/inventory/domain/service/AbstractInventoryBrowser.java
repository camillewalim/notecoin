package notecoin.inventory.domain.service;

import java.util.List;

import notecoin.inventory.domain.model.InventoryQuantity;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryBrowser{
	
	List<InventoryQuantity> getAll();
	InventoryQuantity get(String name);

}
