package notecoin.inventory.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.lang.Nullable;

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
	private AbstractInventoryUpdater updater;

	@Override
	public List<Instruction> getAll() {
		return instructionDao.findAll();
	}

	@Override
	@Deprecated
	public Instruction get(String name) {
		return instructionDao
			.findByProduct(new Product(name))
			.orElseThrow(()-> new NoSuchElementException(name + "do not exist in the inventory"));
	}

	@Override
	public Map<String,  Map<Date, Integer>> getPositions(PositionType type, @Nullable String name, @Nullable Date date){ 
		throw new RuntimeException("not implemented");
	}
	
}
