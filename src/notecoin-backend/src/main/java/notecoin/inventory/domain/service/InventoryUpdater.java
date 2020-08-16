package notecoin.inventory.domain.service;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryUpdater implements AbstractInventoryUpdater{
	
	private InstructionRepository quantityDao;
	
	@Override
	public Instruction update(String name, int quantity) {
		return quantityDao
			.findByProduct(new Product(name))
			.map(iq -> {
				iq.setQuantity(quantity);
				return iq;
			})
			.orElseThrow(() -> new IllegalArgumentException("Feature not implemented"));
	}
	
}
