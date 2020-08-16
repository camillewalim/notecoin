package notecoin.inventory.domain.service;

import notecoin.inventory.domain.model.InventoryName;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
public interface AbstractInventoryCreator{
	
	InventoryName create(String name, String category, String subcategory);

}
