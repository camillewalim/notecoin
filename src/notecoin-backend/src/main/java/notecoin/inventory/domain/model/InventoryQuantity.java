package notecoin.inventory.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author camille.walim
 * 
 * "[...] quantity information'"
 */
@Entity
@NoArgsConstructor @Getter
public class InventoryQuantity {
	
	public InventoryQuantity(int quantity, InventoryName name) {
		setQuantity(quantity);
		setName(name);
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private int quantity;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "NAME")
	private InventoryName name;
	

	public void setQuantity(int quantity) {
		if(quantity < 1) throw new IllegalStateException("a quantity should be positive");
		this.quantity = quantity;
	}

	public void setName(InventoryName name) {
		if(name==null) throw new IllegalStateException("a quantity inventory should be properly named");
		this.name = name;
	}
}
