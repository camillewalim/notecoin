package notecoin.inventory.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author camille.walim
 * 
 * category, sub-category [...] some validation rules when creating an inventory, eg. a sub-category "Shoe" should not in category "Food"
 */
@Entity
@NoArgsConstructor @AllArgsConstructor @Builder @Getter 
public class InventoryCategory {
	
	@Id
	private String name;
	
	@ManyToOne(fetch=FetchType.EAGER) @Setter
	private InventoryCategory supercategory;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "SUPERCATEGORY_NAME",nullable=true)
	private List<InventoryCategory> subcategories;
	
	
	public static List<String> getPath(InventoryCategory cat){
		return getPath(cat, new ArrayList<>());
	}
	
	private static List<String>  getPath(InventoryCategory cat, List<String> list){
		list.add(cat.getName());
		if(cat.getSupercategory()!=null) 
			getPath(cat.getSupercategory(), list);
		return list;
	}
	
	public static InventoryCategory createPath(String...categories){
		return Stream	
			.of(categories)
			.map(name -> new InventoryCategory(name, null, new ArrayList<>()))
			.reduce((cat0, cat1) ->{
				cat1.setSupercategory(cat0);
				cat0.getSubcategories().add(cat1);
				return cat1;
			})
			.get();
	}
}
