package notecoin.inventory.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author camille.walim
 * An inventory has a name
 */
@Entity
@NoArgsConstructor  @AllArgsConstructor @Builder @Getter
public class InventoryName {
	
	@Id
	private String name;

	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "CATEGORY_NAME") 
	@Setter
	private InventoryCategory category;
}
