package notecoin.inventory.domain.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 * "[...] quantity information'"
 */
@Entity
@NoArgsConstructor @Getter
public class InventoryQuantity {
	
	public InventoryQuantity(int quantity, Product product) {
		setQuantity(quantity);
		setProduct(product);
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private int quantity;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "PRODUCT")
	@JsonIgnore
	private Product product;
	

	public void setQuantity(int quantity) {
		if(quantity < 0) throw new IllegalStateException("a quantity should be positive");
		this.quantity = quantity;
	}

	public void setProduct(Product product) {
		if(product==null) throw new IllegalStateException("a quantity inventory should be properly named");
		this.product = product;
	}
}
