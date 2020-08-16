package notecoin.inventory.domain.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author camille.walim
 * 
 * an inventory is a collection of products: so we could extends its design with new properties about them.
 * Ex: a banana is not only a "banana" but has also some color, weight, height, origin, etc.
 */
@Entity
@NoArgsConstructor  @AllArgsConstructor @Builder @Getter
@TypeDef(
    name = "jsonb",
    typeClass = JsonBinaryType.class
)
public class Product {
	
	/** used for reference */
	public Product(String name) {this.name = name;}
	/** used for creation through categorisation */
	public Product(String name, ProductClass category) {this(name); this.category = category; this.creation = new Date(); }
	
	@Id
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name = "CATEGORY_NAME") 
	@Setter
	@JsonIgnore
	private ProductClass category;

	private Date creation;		//when had this product being creating in inventory
	private String origin;			//where is this kind of product produced
	private double price;			//has this product a price
	private String currency;		//price Unit
	
	@Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
	private ProductDetailsAbstract details;
	
}
