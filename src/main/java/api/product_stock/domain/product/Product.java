package api.product_stock.domain.product;

import api.product_stock.domain.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Table(name = "products")
@Entity(name = "Product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(ProductRegistrationData data, Category category) {
        this.name = data.name();
        this.price = data.price();
        this.stock = data.stock();
        this.category = category;
        this.active = true;
    }

    public void updateInformation(ProductUpdateData data, Category newCategory) {
        if (data.name() != null && !data.name().isBlank()) {
            this.name = data.name();
        }
        if (data.price() != null) {
            this.price = data.price();
        }
        if (data.stock() != null) {
            this.stock = data.stock();
        }
        if (newCategory != null && !newCategory.equals(this.category)) {
            this.category.decrementProductCount();
            newCategory.incrementProductCount();
            this.category = newCategory;
        }
    }

    public void delete() {
        this.active = false;
    }
}