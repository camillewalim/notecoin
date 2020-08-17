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
import notecoin.inventory.domain.service.AbstractInventoryUpdater.PositionMemory;
import notecoin.inventory.domain.service.InventoryBrowser;
import notecoin.inventory.domain.service.InventoryCreator;
import notecoin.inventory.domain.service.InventoryUpdater;
import notecoin.inventory.infra.data.InstructionRepository;

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
	@MockBean	InstructionRepository instructionDao;
	@MockBean	PositionMemory positionMemory;
	
	

	@Test
	void create_product_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.put("/inventory/product/create?product=banana&category=vegetable&subcategory=fruit"))
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
	void patch_product_details_2xx() throws Exception {
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}
	
	@Test @Deprecated // MK-I feature
	void post_instruction_without_create_4xx() throws Exception {
		web	.perform(MockMvcRequestBuilders.post("/inventory/update?product=banana&quantity=200"))
		      .andExpect(status().isBadRequest());
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}
	
	@Test
	void post_instruction_200() throws Exception{ // by analogy this test also integration of take / borrow / lend 
		web	.perform(MockMvcRequestBuilders.post("/inventory/instruction?product=banana&quantity=200&type=GIVE&when=2020-10-31"))
	      .andExpect(status().isOk());
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}

	@Test
	void browse_all_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/instructions"))
		      .andExpect(status().isOk());
		// Service activation
			verify(browser,times(1)).getAll();
		// Service query CRUD one time
			verify(instructionDao,times(1)).findAll();
	}


	@Test
	void position_all_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/position?type=InStock&product=banana&when=2020-10-31"))
		      .andExpect(status().isOk());
		// Service activation
			verify(browser,times(1)).getPositions(any(),any(),any());
		// Stay in the API rather than locking the DB
			verify(updater,times(1)).getPositionCopiedMap();
//			verify(instructionDao,times(1)).findAll();
	}
	

	@Test
	void browse_nonexisting_4xx() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/browse?product=random"))
		      .andExpect(status().isNotFound());
		// Could not test in a transient environment -> a bit long to code to emulate this transactionality
	}
	
}
