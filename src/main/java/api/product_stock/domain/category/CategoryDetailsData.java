package api.product_stock.domain.category;

public record CategoryDetailsData(Long id, String name, Integer quantidadeProdutos) {

    public CategoryDetailsData(Category category) {
        this(category.getId(), category.getName(), category.getProductCount());
    }
}


