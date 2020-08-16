package notecoin.inventory.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryBrowserUT {

	String banana="banana";
	
	InventoryQuantityRepository quantityDao= mock(InventoryQuantityRepository.class);
	InventoryBrowser service = new InventoryBrowser(quantityDao);
	
	InventoryName banana_name = new InventoryName(banana, new InventoryCategory());
	InventoryQuantity banana_quantity = new InventoryQuantity(100, banana_name);

	{
		when(quantityDao.findByName(any())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, InventoryName.class).getName()==banana ? banana_quantity :	null));
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
