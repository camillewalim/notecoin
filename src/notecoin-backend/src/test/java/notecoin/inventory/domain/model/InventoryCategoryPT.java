package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static notecoin.inventory.domain.model.InventoryCategory.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author camille.walim
 * 
 * Property-Based test
 */
class InventoryCategoryPT {

	@Test
	void isMonadAndRecursive() {
		String vegetable = "vegetable";
		String fruit = "fruit";
		String berry = "berry";
		
		assertEquals(
			getPath(createPath(vegetable,fruit,berry)),
			Arrays.asList(berry,fruit,vegetable));
	}

}
