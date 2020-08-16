package notecoin.inventory.domain.service;

import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
public interface AbstractInventoryCreator{
	
	Product create(String name, String category, String subcategory);

}
