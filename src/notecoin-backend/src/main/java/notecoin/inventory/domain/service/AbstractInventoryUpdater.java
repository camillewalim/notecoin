package notecoin.inventory.domain.service;

import java.util.Date;

import org.springframework.lang.Nullable;

import notecoin.inventory.domain.model.Instruction;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryUpdater{
	
	@Deprecated // Mk-I feature (inventory non-event based)
	Instruction update(String name, int quantity);
	
	Instruction instruction(String name, Type type, int quantity, Date date, @Nullable Date until);
	
	public static enum Type{
		GIVE, TAKE, LEND, BORROW
	}
}
