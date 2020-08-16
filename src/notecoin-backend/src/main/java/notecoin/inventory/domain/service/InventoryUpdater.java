package notecoin.inventory.domain.service;

import java.util.Date;

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
	@Deprecated // Mk-I feature (inventory non-event based)
	public Instruction update(String name, int quantity) {
		return quantityDao
			.findByProduct(new Product(name))
			.map(iq -> {
				iq.setQuantity(quantity);
				return iq;
			})
			.orElseThrow(() -> new IllegalArgumentException("Feature not implemented"));
	}
	
	@Override
	public Instruction instruction(String name, AbstractInventoryUpdater.Type type, int quantity, Date date, Date until) {
		throw new RuntimeException("not implemented");
	}
	
}
