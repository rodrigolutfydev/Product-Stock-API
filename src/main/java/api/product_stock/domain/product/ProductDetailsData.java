package api.product_stock.domain.product;

import java.math.BigDecimal;

public record ProductDetailsData(Long id, String name, BigDecimal price, Integer stock) {
    public ProductDetailsData(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }
}
