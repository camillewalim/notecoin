package notecoin.inventory.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryName;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.domain.service.AbstractInventoryCreator;
import notecoin.inventory.domain.service.AbstractInventoryUpdater;
import notecoin.inventory.domain.service.InventoryCreator;
import notecoin.inventory.domain.service.InventoryUpdater;
import notecoin.inventory.infra.data.InventoryQuantityRepository;

/**
 * @author camille.walim
 * 
 * All Services dedicated to Inventory (and their autowiring relations) are listed there for convenience.
 */
@Configuration
public class InventoryConfig {

	@Bean
	public AbstractInventoryCreator creator(
		JpaRepository<InventoryName, String> memory,
		JpaRepository<InventoryCategory, String> categoryDao,
		JpaRepository<InventoryQuantity, Integer> quantityDao) {
		return new InventoryCreator(memory, categoryDao, quantityDao);
	}
	

	@Bean
	public AbstractInventoryUpdater updater(JpaRepository<InventoryQuantity, Integer> quantityDao) {
		return new InventoryUpdater((InventoryQuantityRepository) quantityDao);
	}
	
}
