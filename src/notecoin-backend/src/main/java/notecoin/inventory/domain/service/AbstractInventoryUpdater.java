package notecoin.inventory.domain.service;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.util.Pair;
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
	
	Instruction instruction(String name, OrderType type, int quantity, Date date, @Nullable Date until);
	
	/**
	 * a copy of the current positions -> none of the objects present there should be in common with the core one used for writing
	 * (to avoid deadlock issues)
	 */
	Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>> getPositionCopiedMap();
	
	public static enum OrderType{
		GIVE, TAKE, LEND, BORROW
	}
}
