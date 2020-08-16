package notecoin.inventory.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.infra.data.InventoryNameRepository;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryUpdaterUT {

	String banana="banana";
	
	InventoryQuantityRepository quantityDao= mock(InventoryQuantityRepository.class);
	InventoryNameRepository nameDao= mock(InventoryNameRepository.class);
	InventoryUpdater service = new InventoryUpdater(nameDao, quantityDao);
	
	InventoryName banana_name = new InventoryName(banana, new InventoryCategory());
	InventoryQuantity banana_quantity = new InventoryQuantity(100, banana_name);

	{
		when(nameDao.findById(anyString())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, String.class)==banana ? banana_name: null));
		when(quantityDao.findByName(anyString())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, String.class)==banana ? banana_quantity :	null));
	}
	
	@Test
	public void updateQuantity() {
		service.update(banana, 200);
		assertEquals(banana_quantity.getQuantity(), 200);
	}
	
	@Test
	public void blockUpdateQuantityIfInventoryDoNotExist() {
		assertThrows(IllegalArgumentException.class, ()-> service.update("random", 200));
	}
}