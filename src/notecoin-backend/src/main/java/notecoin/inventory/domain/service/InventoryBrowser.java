package notecoin.inventory.domain.service;

import java.util.List;
import java.util.NoSuchElementException;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 *"to browse Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryBrowser implements AbstractInventoryBrowser{
	
	private InstructionRepository instructionDao;

	@Override
	public List<Instruction> getAll() {
		return instructionDao.findAll();
	}

	@Override
	public Instruction get(String name) {
		return instructionDao
			.findByProduct(new Product(name))
			.orElseThrow(()-> new NoSuchElementException(name + "do not exist in the inventory"));
	}
	
	
}
