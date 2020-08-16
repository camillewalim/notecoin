package notecoin.inventory.domain.model.product;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor  @Getter
public class ProductDetailsFruit extends ProductDetailsVegetable{
	
	public ProductDetailsFruit(
		String color, double average_weight, double average_height, 
		double sugar_level
	) {
		super(color, average_weight, average_height);
		this.sugar_level = sugar_level;
	}
	
	private double sugar_level;
}
