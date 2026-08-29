package api.product_stock.domain.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRegistrationData(
        @NotBlank(message = "O name da categoria é obrigatório")
        String name
) {}