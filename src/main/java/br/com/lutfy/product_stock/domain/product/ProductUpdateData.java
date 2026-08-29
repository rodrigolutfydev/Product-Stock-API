package br.com.lutfy.product_stock.domain.product;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;


public record ProductUpdateData(
        Long id,
        @NotBlank
        String name,
        BigDecimal price,
        Integer stock,
        Long categoryId
    ) {
}
