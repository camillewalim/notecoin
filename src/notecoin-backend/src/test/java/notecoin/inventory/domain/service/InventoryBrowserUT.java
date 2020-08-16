package notecoin.inventory.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryBrowserUT {

	String banana="banana";
	
	InstructionRepository quantityDao= mock(InstructionRepository.class);
	InventoryBrowser service = new InventoryBrowser(quantityDao);
	
	Product banana_name = new Product(banana, new ProductClass());
	Instruction banana_quantity = new Instruction(100, banana_name, Instruction.Type.given, new Date(), null);

	{
		when(quantityDao.findByProduct(any())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, Product.class).getName()==banana ? banana_quantity :	null));
		when(quantityDao.findAll()).thenAnswer(i -> Collections.singletonList(banana_quantity));
	}
	
	@Test
	public void browseAll() {
		assertEquals(service.getAll(), Collections.singletonList(banana_quantity));
	}
	

	@Test
	public void browseExisting() {
		assertEquals(service.get(banana), banana_quantity);
	}
	
	@Test
	public void blockUnknown() {
		assertThrows(NoSuchElementException.class, ()-> service.get("random"));
	}
}
