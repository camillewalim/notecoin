package notecoin.inventory.infra.data;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface ProductRepository extends JpaRepository<Product, String>{}