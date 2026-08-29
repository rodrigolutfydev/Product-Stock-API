package api.product_stock.domain.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ProductRegistrationData(
        @NotBlank(message = "O name do produto é obrigatório")
        String name,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "O stock é obrigatório")
        @PositiveOrZero(message = "O stock não pode ser negativo")
        Integer stock,

        @NotNull(message = "A categoria é obrigatória")
        Long categoryId
) {
}