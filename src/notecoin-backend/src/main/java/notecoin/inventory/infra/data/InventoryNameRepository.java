package notecoin.inventory.infra.data;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.InventoryName;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface InventoryNameRepository extends JpaRepository<InventoryName, String>{}