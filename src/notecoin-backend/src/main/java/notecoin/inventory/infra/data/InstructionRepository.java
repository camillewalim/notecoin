package notecoin.inventory.infra.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 * JPA repository
 */
public interface InstructionRepository extends JpaRepository<Instruction, Integer> {
	
	Optional<Instruction> findByProduct(Product product);
	
}