package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author camille.walim
 * Property-Based test
 */
class InventoryNamePT {

	@Test
	void hasAName() {
		String banana = "banana";
		assertEquals(new InventoryName(banana).getName(),banana);
	}

}
