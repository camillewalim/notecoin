package notecoin.inventory.domain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.domain.model.product.ProductClass;
import notecoin.inventory.domain.model.product.ProductDetailsAbstract;

/**
 * @author camille.walim
 * 
 *"to create Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryCreator implements AbstractInventoryCreator{
	
	private JpaRepository<Product, String> productDao;
	private JpaRepository<ProductClass, String> productClassDao;
	
	@SuppressWarnings("unchecked")
	private final static Map<String, String> productClassInJava = Stream
		.of(((JsonSubTypes)ProductDetailsAbstract.class.getAnnotation(JsonSubTypes.class)).value())
		.map(type -> ProductClass.detectPath((Class<? extends ProductDetailsAbstract>)type.value(), new ArrayList<>()))
		.collect(HashMap::new, (m,path)->m.put(path.get(0), path.size()>1 ? path.get(1): null), HashMap::putAll);
	
	public Product create(String name, String category, @Nullable String subcategory) {
		if(name==null || category==null) 
			throw new IllegalArgumentException("name or category could not be null");
		
		String thiscategory = Optional.ofNullable(subcategory).orElse(category);
		String supercategory = Optional.ofNullable(subcategory).map(c -> category).orElse(null);
		
		ProductClass category_c = productClassDao.findById(thiscategory).orElse(null);
		
		if(Optional	.ofNullable(category_c)
					.map(c -> supercategory!=null && ! c.getSupercategory().getName().equals(supercategory))
					.orElse(false))
			throw new IllegalArgumentException(thiscategory + " is already a sub-category of " + category_c.getSupercategory().getName());
		
		if(Optional	.ofNullable(productClassInJava.get(thiscategory))
					.map(supercategoryInJava -> supercategory != null && ! supercategoryInJava.equals(supercategory))
					.orElse(false))
			throw new IllegalArgumentException(thiscategory + " is already a sub-category (in JAVA) of " + productClassInJava.get(thiscategory));
		
		return productDao.findById(name)
			.map(name_n -> {
				if(Optional	.ofNullable(name_n)
					.map(n -> ! n.getCategory().getName().equals(thiscategory))
					.orElse(false)) 
					throw new IllegalArgumentException(name + " is already a name of category " + name_n.getCategory().getName());
				return name_n;
			})
			.orElseGet(()->{
				
				ProductClass name_category = Optional	
					.ofNullable(category_c)
					.orElseGet(() -> {
						ProductClass supercategory_c = Optional
							.ofNullable(supercategory)
							.map(id -> productClassDao.findById(id).orElseGet(() -> new ProductClass(supercategory, null, new ArrayList<>()) ))
							.orElse(null);
						
						ProductClass category_c_nonnull = new ProductClass(thiscategory, supercategory_c, new ArrayList<>());
						
						if(supercategory_c!=null) 
							supercategory_c.getSubcategories().add(category_c_nonnull);

						productClassDao.save(supercategory_c!=null ? supercategory_c : category_c_nonnull);
						
						return category_c_nonnull;
					});
				
				Product name_n = new Product(name, name_category); 
				
				productDao.save(name_n);
				
				productClassDao.flush();
				productDao.flush();
				
				return name_n;
			});
	}

	public Product updateDetails(String name,
			String origin, Double price, String currency,
			ProductDetailsAbstract details
		) {
		return productDao.findById(name)
			.map(product -> {
				if(origin!=null)	product.setOrigin(origin);
				if(price!=null)		product.setPrice(price);
				if(currency!=null)	product.setCurrency(currency);
				if(details!=null)	product.setDetails(details);
				productDao.saveAndFlush(product);
				return product;
			})
			.orElseThrow(()-> new NoSuchElementException(name + "do not exist in the inventory"));
	}
}
