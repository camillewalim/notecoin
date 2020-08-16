package notecoin.inventory.domain.service;

import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductDetailsAbstract;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
public interface AbstractInventoryCreator{
	
	Product create(String name, String category, String subcategory);
	Product updateDetails(String name,
		String origin, Double price, String currency,
		ProductDetailsAbstract details
	);
}
