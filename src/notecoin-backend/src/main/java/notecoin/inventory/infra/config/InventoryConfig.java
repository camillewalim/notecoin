package notecoin.inventory.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.service.AbstractInventoryBrowser;
import notecoin.inventory.domain.service.AbstractInventoryCreator;
import notecoin.inventory.domain.service.AbstractInventoryUpdater;
import notecoin.inventory.domain.service.InventoryBrowser;
import notecoin.inventory.domain.service.InventoryCreator;
import notecoin.inventory.domain.service.InventoryUpdater;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 * All Services dedicated to Inventory (and their autowiring relations) are listed there for convenience.
 */
@Configuration
public class InventoryConfig {

	@Bean
	public AbstractInventoryCreator creator(
		JpaRepository<Product, String> productDao,
		JpaRepository<ProductClass, String> productClassDao,
		JpaRepository<Instruction, Integer> instructionDao) {
		return new InventoryCreator(productDao, productClassDao);
	}
	

	@Bean
	public AbstractInventoryUpdater updater(JpaRepository<Instruction, Integer> instructionDao) {
		return new InventoryUpdater((InstructionRepository) instructionDao);
	}
	

	@Bean
	public AbstractInventoryBrowser browser(JpaRepository<Instruction, Integer> instructionDao) {
		return new InventoryBrowser((InstructionRepository) instructionDao);
	}
	
	
}
