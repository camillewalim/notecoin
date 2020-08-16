package notecoin.inventory.domain.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor  @Getter
public class ProductDetailsGreens extends ProductDetailsVegetable{
	
	public ProductDetailsGreens(
		String color, double average_weight, double average_height, 
		double salt_level
	) {
		super(color, average_weight, average_height);
		this.salt_level = salt_level;
	}
	
	private double salt_level;
}
