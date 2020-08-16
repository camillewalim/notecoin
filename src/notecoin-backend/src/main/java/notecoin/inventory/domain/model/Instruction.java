package notecoin.inventory.domain.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 * "[...] quantity information'"
 */
@Entity
@NoArgsConstructor @Getter
public class Instruction {
	
	public Instruction(int quantity, Product product, Type type, Date date, Date maturity) {
		setQuantity(quantity);
		setProduct(product);
		setDate(date);
		this.type = type;
		setMaturity(type==Type.taken || type==Type.given ? this.date : maturity);
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private int quantity;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "PRODUCT")
	@JsonIgnore
	private Product product;
	
	public static enum Type{taken, given, receivable, payable}
	@Enumerated(EnumType.STRING) 
	private Type type; 
	
	@Setter private Date date;
	private Date maturity;

	public void setQuantity(int quantity) {
		if(quantity < 0) throw new IllegalStateException("a quantity should be positive");
		this.quantity = quantity;
	}

	public void setProduct(Product product) {
		if(product==null) throw new IllegalStateException("a quantity inventory should be properly named");
		this.product = product;
	}

	private void setMaturity(Date maturity) {
		if(maturity.getTime() < this.date.getTime()) throw new IllegalStateException("could not mature in the past");
		this.maturity = maturity;
	}
	
}
