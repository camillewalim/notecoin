package notecoin.inventory.infra.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface InventoryQuantityRepository extends JpaRepository<InventoryQuantity, Integer> {
	
	Optional<InventoryQuantity> findByProduct(Product product);
	
}