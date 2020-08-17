package notecoin.inventory.domain.service;

import org.springframework.lang.Nullable;

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
		@Nullable String origin, @Nullable Double price, @Nullable String currency,
		@Nullable ProductDetailsAbstract details
	);
}
