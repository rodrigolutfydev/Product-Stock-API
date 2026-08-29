package br.com.lutfy.product_stock.domain.product;

import java.math.BigDecimal;


public record ProductListingData(Long id, String name, BigDecimal price, Integer stock, Long categoryId) {
    public ProductListingData(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getCategory().getId());
    }
}
