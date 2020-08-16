package notecoin.inventory.domain.model.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author camille.walim
 * 
 * category, sub-category [...] some validation rules when creating an inventory, eg. a sub-category "Shoe" should not in category "Food".
 * 
 * category and sub-category is a taxonomy and by definition : - as it is recursive it could be infinitely long.
	- it represents objects that inherit some properties
	- 2 situations could come then :
		- this categorisation is the result of property inheritance (all bananas have all the fruit's properties) and therefore this inheritance logic is coded through Java polymorphism.
		- this categorisation is arbitrary (a user decided it) : and the DB is responsible to store this relation.

	The two situations cohabitate together. Users are allowed to create as many arbitrary categories as they want as long as they do not contradict the existing hard-coded java polymorphism.
 */
@Entity
@NoArgsConstructor @AllArgsConstructor @Builder @Getter 
public class ProductClass {
	
	@Id
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER) @Setter
	private ProductClass supercategory;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "SUPERCATEGORY_NAME",nullable=true)
	@JsonIgnore
	private List<ProductClass> subcategories;
	
	
	
	
	public static List<String> getPath(ProductClass cat){
		return getPath(cat, new ArrayList<>());
	}
	
	private static List<String>  getPath(ProductClass cat, List<String> list){
		list.add(cat.getName());
		if(cat.getSupercategory()!=null) 
			getPath(cat.getSupercategory(), list);
		return list;
	}
	
	public static ProductClass createPath(String...categories){
		return Stream	
			.of(categories)
			.map(name -> new ProductClass(name, null, new ArrayList<>()))
			.reduce((cat0, cat1) ->{
				cat1.setSupercategory(cat0);
				cat0.getSubcategories().add(cat1);
				return cat1;
			})
			.get();
	}
	public static ProductClass createPath(Class<? extends ProductDetailsAbstract> type){
		List<String> path = createPath(type, new ArrayList<>());
		Collections.reverse(path);
		return createPath(path.toArray(new String[path.size()]));
	}
	private static List<String> createPath(Class<? extends ProductDetailsAbstract> type, List<String> list){
		list.add(type.getSimpleName().replace("ProductDetails","").toLowerCase());
		if(! type.getSuperclass().equals(ProductDetailsAbstract.class))
			return createPath((Class<? extends ProductDetailsAbstract>) type.getSuperclass(), list);
		return list; 
	}
}
