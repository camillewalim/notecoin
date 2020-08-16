package notecoin.inventory.domain.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.InventoryCategory;
import notecoin.inventory.domain.model.InventoryQuantity;
import notecoin.inventory.domain.model.product.Product;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryCreator implements AbstractInventoryCreator{
	
	private JpaRepository<Product, String> nameDao;
	private JpaRepository<InventoryCategory, String> categoryDao;
	private JpaRepository<InventoryQuantity, Integer> quantityDao;
	
	public Product create(String name, String category, @Nullable String subcategory) {
		if(name==null || category==null) 
			throw new IllegalArgumentException("name or category could not be null");
		
		String thiscategory = Optional.ofNullable(subcategory).orElse(category);
		String supercategory = Optional.ofNullable(subcategory).map(c -> category).orElse(null);
		
		InventoryCategory category_c = categoryDao.findById(thiscategory).orElse(null);
		
		if(Optional	.ofNullable(category_c)
					.map(c -> supercategory!=null && ! c.getSupercategory().getName().equals(supercategory))
					.orElse(false))
			throw new IllegalArgumentException(thiscategory + " is already a sub-category of " + category_c.getSupercategory().getName());
		
		return nameDao.findById(name)
			.map(name_n -> {
				if(Optional	.ofNullable(name_n)
					.map(n -> ! n.getCategory().getName().equals(thiscategory))
					.orElse(false)) 
					throw new IllegalArgumentException(name + " is already a name of category " + name_n.getCategory().getName());
				return name_n;
			})
			.orElseGet(()->{
				
				InventoryCategory name_category = Optional	
					.ofNullable(category_c)
					.orElseGet(() -> {
						InventoryCategory supercategory_c = Optional
							.ofNullable(supercategory)
							.map(id -> categoryDao.findById(id).orElseGet(() -> new InventoryCategory(supercategory, null, new ArrayList<>()) ))
							.orElse(null);
						
						InventoryCategory category_c_nonnull = new InventoryCategory(thiscategory, supercategory_c, new ArrayList<>());
						
						if(supercategory_c!=null) 
							supercategory_c.getSubcategories().add(category_c_nonnull);

						categoryDao.save(supercategory_c!=null ? supercategory_c : category_c_nonnull);
						
						return category_c_nonnull;
					});
				
				Product name_n = new Product(name, name_category); 
				
				nameDao.save(name_n);
				quantityDao.save(new InventoryQuantity(0, name_n));
				
				categoryDao.flush();
				nameDao.flush();
				
				return name_n;
			});
	}

}
