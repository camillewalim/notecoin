package notecoin.inventory.infra.data;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.InventoryCategory;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface InventoryCategoryRepository extends JpaRepository<InventoryCategory, String>{}