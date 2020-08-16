package notecoin.inventory.domain.model.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ProductDetailsFruit.class, name = "fruit"),
    @JsonSubTypes.Type(value = ProductDetailsVegetable.class, name = "vegetable"),
    @JsonSubTypes.Type(value = ProductDetailsGreens.class, name = "greens")
})
public class ProductDetailsAbstract implements Serializable {}
