package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductDetailsAbstract;
import notecoin.inventory.domain.model.product.ProductDetailsFruit;
import notecoin.inventory.domain.model.product.ProductDetailsVegetable;

/**
 * @author camille.walim
 * Property-Based test
 */
public class ProductPT {

	@Test
	void hasAName() {
		String banana = "banana";
		assertEquals(new Product(banana, new InventoryCategory()).getName(),banana);
	}
	
	@Test
	void hasACategory() {
		String fruit = "fruit";
		assertEquals(new Product("banana", new InventoryCategory("fruit", null, new ArrayList<>())).getCategory().getName(),fruit);
	}

	@Test
	void hasGenericAndPolymorphicProperties() {
		Product banana = new Product("banana",
			new InventoryCategory("fruit", null, new ArrayList<>()), 
				new Date(), "Madagascar", 
				12.5, "EUR", 
				new ProductDetailsFruit("yellow", 10, 10, 0.2)
			); 
		assertTrue(banana.getDetails() instanceof ProductDetailsVegetable);
		assertTrue(banana.getDetails() instanceof ProductDetailsAbstract);
	}
}
