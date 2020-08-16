package notecoin.inventory.api.inventory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.service.InventoryBrowser;
import notecoin.inventory.domain.service.InventoryCreator;
import notecoin.inventory.domain.service.InventoryUpdater;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 *
 * Mockito-Integration-Test based of endpoint
 */
@RunWith(SpringRunner.class)
@WebMvcTest
class InventoryIT {

	@Autowired
	MockMvc web;

	@SpyBean	InventoryCreator creator;
	@SpyBean	InventoryUpdater updater;
	@SpyBean	InventoryBrowser browser;
	
	@MockBean	JpaRepository<Product, String> productDao;
	@MockBean	JpaRepository<ProductClass, String> productClassDao;
	@MockBean	InventoryQuantityRepository quantityDao;
	
	

	@Test
	void create_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.put("/inventory/create?product=banana&category=vegetable&subcategory=fruit"))
		      .andExpect(status().isOk());
		// Service activation
			verify(creator,times(1)).create(any(),any(),any());
		// Service check existing taxonomy
			verify(productDao,times(1)).findById(any());
			verify(productClassDao,times(2)).findById(any());
		// Service save one category and one name associated to it.
			verify(productClassDao,times(1)).save(any());
			verify(productDao,times(1)).save(any());
	}
	
	@Test
	void update_without_create_4xx() throws Exception {
		web	.perform(MockMvcRequestBuilders.post("/inventory/update?product=banana&quantity=200"))
		      .andExpect(status().isBadRequest());
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}

	@Test
	void browse_all_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/browse"))
		      .andExpect(status().isOk());
		// Service activation
			verify(browser,times(1)).getAll();
		// Service query CRUD one time
			verify(quantityDao,times(1)).findAll();
	}
	

	@Test
	void browse_nonexisting_4xx() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/browse?product=random"))
		      .andExpect(status().isNotFound());
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}
	
}
