package notecoin.inventory.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.Instruction.Type;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.service.AbstractInventoryBrowser.PositionType;
import notecoin.inventory.domain.service.AbstractInventoryUpdater.PositionMemory;
import notecoin.inventory.infra.data.InstructionRepository;
import notecoin.inventory.infra.data.ProductRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryBrowserUT {

	String banana="banana";
	
	InstructionRepository instructionDao= mock(InstructionRepository.class);
	ProductRepository productDao= mock(ProductRepository.class);
	
	InventoryUpdater updater = new InventoryUpdater(instructionDao, productDao, new PositionMemory());  
	InventoryBrowser service = new InventoryBrowser(instructionDao, updater);
	
	
	Product banana_product = new Product(banana, new ProductClass());
	Instruction banana_quantity = new Instruction(100, banana_product, Instruction.Type.given, new Date(), null);

	{
		when(instructionDao.findByProduct(any())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, Product.class).getName()==banana ? banana_quantity :	null));
		when(instructionDao.findAll()).thenAnswer(i -> Collections.singletonList(banana_quantity));
		when(productDao.findById(anyString())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, String.class)==banana ? banana_product: null));
	}

	Date today = new Date(new Date().getTime()+1000000);
	Date tomorrow = 		new Date(today.getTime()+TimeUnit.DAYS.toMillis(1));
	Date aftertomorrow = 	new Date(today.getTime()+TimeUnit.DAYS.toMillis(2));
	Date later = 			new Date(today.getTime()+TimeUnit.DAYS.toMillis(3));
	{
		
		updater.instructionLoad(new Instruction(500, new Product(banana), Type.given, today, null));
		updater.instructionLoad(new Instruction(100, new Product(banana), Type.taken, later, null));
		updater.instructionLoad(new Instruction(500, new Product(banana), Type.given, tomorrow, null));
		updater.instructionLoad(new Instruction(500, new Product(banana), Type.taken, aftertomorrow, null));
		updater.instructionLoad(new Instruction(100, new Product(banana), Type.receivable, today, later));
		updater.instructionLoad(new Instruction(400, new Product(banana), Type.receivable, tomorrow, aftertomorrow));
		updater.instructionLoad(new Instruction(200, new Product(banana), Type.payable, today, later));
		updater.instructionLoad(new Instruction(200, new Product(banana), Type.payable, tomorrow, later));
	}
	
	@Test
	public void browseAll() {
		assertEquals(service.getAll(), Collections.singletonList(banana_quantity));
	}
	
	@Test
	public void getPositionInStock() {
		HashMap<Date, Integer> positionsByDate = new HashMap<>();
		positionsByDate.put(new Date(0), 	0);
		positionsByDate.put(today, 			400);
		positionsByDate.put(tomorrow, 		1000);
		positionsByDate.put(aftertomorrow, 	200);
		positionsByDate.put(later, 			400);
		assertEquals(service.getPositions(PositionType.InStock, banana, null).get(banana), positionsByDate);
	}
	
	@Test
	public void getPositionInLnB() {
		HashMap<Date, Integer> positionsByDate = new HashMap<>();
		positionsByDate.put(new Date(0), 	0);
		positionsByDate.put(today, 			0);
		positionsByDate.put(tomorrow, 		100);
		positionsByDate.put(aftertomorrow, 	0);
		positionsByDate.put(later, 			0);
		assertEquals(service.getPositions(PositionType.LnB, banana, null).get(banana), positionsByDate);
	}
	
	
	@Test @Deprecated
	public void browseExisting() {
		assertEquals(service.get(banana), banana_quantity);
	}
	
	
	@Test
	public void blockUnknown() {
		assertThrows(NoSuchElementException.class, ()-> service.get("random"));
	}
}
