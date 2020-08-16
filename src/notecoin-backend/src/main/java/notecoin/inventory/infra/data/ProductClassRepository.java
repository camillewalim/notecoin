package notecoin.inventory.infra.data;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.product.ProductClass;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface ProductClassRepository extends JpaRepository<ProductClass, String>{}