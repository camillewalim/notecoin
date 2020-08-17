package notecoin.inventory.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;

import notecoin.inventory.domain.model.Instruction;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryBrowser{
	
	List<Instruction> getAll();
	Instruction get(String name);

	Map<String, Map<Date, Integer>> getPositions(PositionType type, @Nullable String name, @Nullable Date date);
	
	public static enum PositionType{
		InStock,// p = taken - given - (some payable) 
		LnB,	// p = receivable - (some payable) 
	}
}
