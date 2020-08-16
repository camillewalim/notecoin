package notecoin.inventory.domain.service;

import java.util.List;

import notecoin.inventory.domain.model.Instruction;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryBrowser{
	
	List<Instruction> getAll();
	Instruction get(String name);

}
