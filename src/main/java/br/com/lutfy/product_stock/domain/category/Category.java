package br.com.lutfy.product_stock.domain.category;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Table(name = "categories")
@Entity(name = "Category")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Boolean active;
    private Integer productCount;

    public Category(CategoryRegistrationData date) {
        this.name = date.name();
        this.active = true;
        this.productCount = 0;
    }

    public void updateInformation(@Valid CategoryUpdateData date  ) {
        if (date.name() != null && !date.name().isBlank()) {
            this.name = date.name();
        }
    }

    public void delete() {
        this.active = false;
    }

    public void incrementProductCount() {
        this.productCount++;
    }

    public void decrementProductCount() {
        this.productCount--;
    }
}

