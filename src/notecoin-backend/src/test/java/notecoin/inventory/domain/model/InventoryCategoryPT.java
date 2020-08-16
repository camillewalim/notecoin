package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

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
			InventoryCategory.getPath(
			Stream	.of(vegetable,fruit,berry)
					.map(name -> new InventoryCategory(name, null, new ArrayList<>()))
					.reduce((cat0, cat1) ->{
						cat1.setSupercategory(cat0);
						cat0.getSubcategories().add(cat1);
						return cat1;
					})
					.get()),
			Arrays.asList(berry,fruit,vegetable));
	}

}
