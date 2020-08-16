package notecoin.inventory.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.service.AbstractInventoryUpdater.Type;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryUpdaterUT {

	String banana="banana";
	
	InstructionRepository quantityDao= mock(InstructionRepository.class);
	InventoryUpdater service = new InventoryUpdater(quantityDao);
	
	Product banana_name = new Product(banana, new ProductClass());
	Instruction banana_quantity = new Instruction(100, banana_name, Instruction.Type.given, new Date(), null);

	{
		when(quantityDao.findByProduct(any())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, Product.class).getName()==banana ? banana_quantity :	null));
	}
	
	@Test @Deprecated // Mk-I feature (inventory non-event based)
	public void updateQuantity() {
		service.update(banana, 200);
		assertEquals(banana_quantity.getQuantity(), 200);
	}
	
	@Test @Deprecated // Mk-I feature (inventory non-event based)
	public void blockUpdateQuantityIfInventoryDoNotExist() {
		assertThrows(IllegalArgumentException.class, ()-> service.update("random", 200));
	}
	
	@Test
	public void couldOrderNow(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+1000000);
		service.instruction(banana, Type.GIVE, 200, today, null);
		service.instruction(banana, Type.TAKE, 100, today, null);
		service.instruction(banana, Type.LEND, 50, today, tomorrow);
		service.instruction(banana, Type.BORROW, 150, today, tomorrow);
	}
	
	@Test
	public void couldOrderForFuture(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+1000000);
		Date aftertomorrow = 	new Date(today.getTime()+2000000);
		service.instruction(banana, Type.GIVE, 200, tomorrow, null);
		service.instruction(banana, Type.TAKE, 100, tomorrow, null);
		service.instruction(banana, Type.LEND, 50, tomorrow, aftertomorrow);
		service.instruction(banana, Type.BORROW, 150, tomorrow, aftertomorrow);
	}
	
	@Test
	public void couldLendFromBorrow(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+1000000);
		service.instruction(banana, Type.LEND, 50, today, tomorrow);
		service.instruction(banana, Type.BORROW, 50, today, tomorrow);
	}

	@Test
	public void blockIfNotAllowedInInventory() {
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction("random", Type.GIVE, 200, today, null));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction("random", Type.LEND, 200, today, tomorrow));
	}
	
	@Test
	public void blockSellingNotInCurrentInventory(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.TAKE, 200, today, null));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.BORROW, 200, today, tomorrow));
	}

	@Test
	public void blockSellingNotEnoughInCurrentInventory(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		service.instruction(banana, Type.GIVE, 50, today, null);
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.TAKE, 200, today, null));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.BORROW, 200, today, tomorrow));
	}

	@Test
	public void blockSellingNotInFutureInventory(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		Date aftertomorrow = 	new Date(today.getTime()+TimeUnit.DAYS.toMillis(2));
		service.instruction(banana, Type.GIVE, 200, today, null);
		service.instruction(banana, Type.TAKE, 200, tomorrow, null);
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.TAKE, 200, tomorrow, null));
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.BORROW, 200, tomorrow, aftertomorrow));
	}
	
	@Test
	public void blockShortSelling(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		service.instruction(banana, Type.LEND, 200, today, tomorrow);
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.TAKE, 50, today, tomorrow));
	}

	@Test
	public void blockPartialShortSelling(){
		Date today = new Date();
		Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
		Date aftertomorrow = 	new Date(today.getTime()+TimeUnit.DAYS.toMillis(2));
		Date later = 			new Date(today.getTime()+TimeUnit.DAYS.toMillis(3));
		service.instruction(banana, Type.LEND, 200, today, aftertomorrow);
		assertThrows(IllegalArgumentException.class, ()-> service.instruction(banana, Type.BORROW, 200, tomorrow, later));
	}
	
}
