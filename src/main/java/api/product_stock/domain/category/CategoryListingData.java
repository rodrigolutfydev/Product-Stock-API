package api.product_stock.domain.category;

public record CategoryListingData(Long id, String name, Integer productCount) {
    public CategoryListingData(Category category) {
        this(category.getId(), category.getName(), category.getProductCount());
    }
}
