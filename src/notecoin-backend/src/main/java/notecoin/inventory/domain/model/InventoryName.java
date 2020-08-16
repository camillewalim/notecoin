package notecoin.inventory.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author camille.walim
 * An inventory has a name
 */
@Entity
@Data @AllArgsConstructor @Builder
public class InventoryName {
	
	@Id
	private String name;
}
