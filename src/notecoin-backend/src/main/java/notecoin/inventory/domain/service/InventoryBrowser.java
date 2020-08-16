package notecoin.inventory.domain.service;

import java.util.List;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 *"to browse Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryBrowser implements AbstractInventoryBrowser{
	
	private InventoryQuantityRepository quantityDao;

	@Override
	public List<InventoryQuantity> getAll() {
		return quantityDao.findAll();
	}

	@Override
	public InventoryQuantity get(String name) {
		return quantityDao
			.findByProduct(new Product(name))
			.orElseThrow(()-> new NoSuchElementException(name + "do not exist in the inventory"));
	}
	
	
}
