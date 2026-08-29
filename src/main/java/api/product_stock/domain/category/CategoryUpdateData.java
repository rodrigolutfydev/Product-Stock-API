package api.product_stock.domain.category;

import jakarta.validation.constraints.NotNull;


public record CategoryUpdateData(
        @NotNull Long id,
        String name
    ) {
}
