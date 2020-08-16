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

import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.service.InventoryCreator;

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
	@MockBean	JpaRepository<InventoryName, String> nameDao;
	@MockBean	JpaRepository<InventoryCategory, String> catDao;

	@Test
	void ping_200() throws Exception {
		web	.perform(MockMvcRequestBuilders.get("/inventory/create?name=banana&category=vegetable&subcategory=fruit"))
		      .andExpect(status().isOk());
		// Service activation
			verify(creator,times(1)).create(any(),any(),any());
		// Service check existing taxonomy
			verify(nameDao,times(1)).findById(any());
			verify(catDao,times(1)).findById(any());
		// Service save one category and one name associated to it.
			verify(catDao,times(1)).save(any());
			verify(nameDao,times(1)).save(any());
	}
	
}
