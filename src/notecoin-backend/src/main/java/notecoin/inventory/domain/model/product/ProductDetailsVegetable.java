package notecoin.inventory.domain.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor  @AllArgsConstructor @Getter
public class ProductDetailsVegetable extends ProductDetailsAbstract{
	private String color;
	private double average_weight;
	private double average_height;
}
