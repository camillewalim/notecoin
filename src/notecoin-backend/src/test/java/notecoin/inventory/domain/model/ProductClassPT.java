package notecoin.inventory.domain.model;

import static notecoin.inventory.domain.model.product.ProductClass.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import notecoin.inventory.domain.model.product.ProductDetailsFruit;

/**
 * @author camille.walim
 * 
 * Property-Based test
 */
class ProductClassPT {

	@Test
	void isMonadAndRecursive() {
		String vegetable = "vegetable";
		String fruit = "fruit";
		String berry = "berry";
		
		assertEquals(
			getPath(createPath(vegetable,fruit,berry)),
			Arrays.asList(berry,fruit,vegetable));
	}
	
	@Test
	void couldRepresentJavaPolymorphism() {
		String vegetable = "vegetable";
		String fruit = "fruit";
		
		assertEquals(
			getPath(createPath(vegetable,fruit)),
			getPath(createPath(ProductDetailsFruit.class)));
	}

}
