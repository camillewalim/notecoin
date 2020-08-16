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
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InventoryCategoryRepository;
import notecoin.inventory.infra.data.ProductRepository;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 * Unit-Test
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class InventoryCreatorUT {

	String vegetable="vegetable", fruit="fruit", banana="banana";
	

	InventoryCategoryRepository catDao = mock(InventoryCategoryRepository.class);
	ProductRepository nameDao= mock(ProductRepository.class);
	InventoryQuantityRepository quantityDao= mock(InventoryQuantityRepository.class);
	InventoryCreator service = new InventoryCreator(nameDao, catDao, quantityDao);
	
	InventoryCategory fruit_cat = InventoryCategory .createPath(vegetable, fruit);
	Product banana_name = new Product(banana, fruit_cat);
	{
		when(catDao.findById(anyString())).thenAnswer(i -> Optional.ofNullable(
					i.getArgument(0, String.class)==fruit ? fruit_cat
				: 	i.getArgument(0, String.class)==vegetable ? fruit_cat.getSupercategory()
				:	null));
		when(nameDao.findById(anyString())).thenAnswer(i -> Optional.ofNullable(i.getArgument(0, String.class)==banana ? banana_name: null));
	}
	
	@Test
	public void createInventoryFromExistingCategory() {
		Product result = service.create("apple", fruit, null);
		assertEquals(result.getCategory(), fruit_cat);
	}
	
	@Test
	public void createInventoryFromNonExistingCategory() {
		Product result = service.create("cucumber", "greens", null);
		assertEquals(result.getCategory().getName(), "greens");
	}
	
	@Test
	public void createInventoryFromNonExistingSubCategoryWithExistingParent() {
		Product result = service.create("cucumber", vegetable, "greens");
		assertEquals(result.getCategory().getName(), "greens");
		assertEquals(result.getCategory().getSupercategory(), fruit_cat.getSupercategory());
	}
	
	@Test
	public void createInventoryFromNonExistingSubCategoryWithNonExistingParent() {
		Product result = service.create("car", "machine", "vehicule");
		assertEquals(result.getCategory().getName(), "vehicule");
		assertEquals(result.getCategory().getSupercategory().getName(), "machine");
	}
	
	@Test
	public void ignoreCreateInventoryAlreadyExisting() {
		Product result = service.create(banana, "vegetable", "fruit");
		assertEquals(result, banana_name);
	}

	@Test
	public void blockCreateInventoryWithoutCategory() {
		assertThrows(IllegalArgumentException.class, ()-> service.create(banana, null, null));
	}
	
	@Test
	public void blockCreateInventoryWithUpdatedCategory() {
		assertThrows(IllegalArgumentException.class, ()-> service.create(banana, "random", null));
		assertThrows(IllegalArgumentException.class, ()-> service.create(banana, "vegetable", null));
		assertThrows(IllegalArgumentException.class, ()-> service.create(banana, "vegetable", "random"));
	}
}
