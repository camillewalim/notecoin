package notecoin.inventory.domain.service;

import org.springframework.data.jpa.repository.JpaRepository;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryName;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryCreator implements AbstractInventoryCreator{
	
	private JpaRepository<InventoryName, String> nameDao;
	private JpaRepository<InventoryCategory, String> categoryDao;
	
	public InventoryName create(String name, String category, String subcategory) {
		throw new RuntimeException("not implemented");
	}

}
