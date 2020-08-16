package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * @author camille.walim
 * Property-Based test
 */
class InventoryNamePT {

	@Test
	void hasAName() {
		String banana = "banana";
		assertEquals(new InventoryName(banana, new InventoryCategory()).getName(),banana);
	}
	
	@Test
	void hasACategory() {
		String fruit = "fruit";
		assertEquals(new InventoryName("banana", new InventoryCategory("fruit", null, new ArrayList<>())).getCategory().getName(),fruit);
	}

}
