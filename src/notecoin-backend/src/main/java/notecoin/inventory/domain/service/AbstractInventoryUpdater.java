package notecoin.inventory.domain.service;

import notecoin.inventory.domain.model.Instruction;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryUpdater{
	
	Instruction update(String name, int quantity);

}
