package notecoin.inventory.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.Test;

import notecoin.inventory.domain.model.product.Product;


/**
 * @author camille.walim
 * 
 * Property-Based test
 */
class InstructionPT {

	@Test
	void hasQuantityAndNameAndDatesAndType() {
		String banana = "banana";
		int quantity = 100;
		Date date = new Date();
		
		Instruction iq = new Instruction(
			quantity, new Product(banana,null),
			Instruction.Type.given, date, date);
		
		assertEquals(iq.getQuantity(), quantity);
		assertEquals(iq.getProduct().getName(), banana);
		assertEquals(iq.getType(), Instruction.Type.given);
		assertEquals(iq.getDate(), date);
	}
	
	@Test
	void maturityIsFetchForInstantTypes() {
		assertNotNull(new Instruction(
				100, new Product("banana",null),
				Instruction.Type.given, new Date(), null)
			.getMaturity());
		assertNotNull(new Instruction(
				100, new Product("banana",null),
				Instruction.Type.taken, new Date(), null)
			.getMaturity());
	}
	
	@Test
	void shouldBePositive() {
		assertThrows(IllegalStateException.class, ()-> new Instruction(-1, new Product("some",null), Instruction.Type.given, new Date(), null ));
	}
	
	@Test
	void shouldBeNamed() {
		assertThrows(IllegalStateException.class, ()-> new Instruction(100, null, Instruction.Type.given, new Date(), null ));
	}

}
