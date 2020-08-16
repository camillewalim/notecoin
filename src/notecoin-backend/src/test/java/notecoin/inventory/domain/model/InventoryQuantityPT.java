package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;


/**
 * @author camille.walim
 * 
 * Property-Based test
 */
class InventoryQuantityPT {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	void hasQuantityAndName() {
		String banana = "banana";
		int quantity = 100;
		InventoryQuantity iq = new InventoryQuantity(quantity, new InventoryName(banana,null));
		assertEquals(iq.getQuantity(), quantity);
		assertEquals(iq.getName().getName(), banana);
	}
	
	@Test
	void shouldBePositive() {
		assertThrows(IllegalStateException.class, ()-> new InventoryQuantity(-1, new InventoryName("some",null)));
	}
	
	@Test
	void shouldBeNamed() {
		assertThrows(IllegalStateException.class, ()-> new InventoryQuantity(100, null));
	}

}
