package br.com.lutfy.product_stock.controller;

import br.com.lutfy.product_stock.domain.category.CategoryRepository;
import br.com.lutfy.product_stock.domain.product.*;
import br.com.lutfy.product_stock.domain.product.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("produtos")
public class ProductController {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid ProductRegistrationData data, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.getReferenceById(data.categoryId());
        category.incrementProductCount();
        categoryRepository.save(category);
        var product = new Product(data, category);
        repository.save(product);
        var uri = uriBuilder.path("/produtos/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(new ProductDetailsData(product));
    }

    @GetMapping
    public ResponseEntity<Page<ProductListingData>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pagination) {
        var page = repository.findAllByActiveTrue(pagination).map(ProductListingData::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid ProductUpdateData data) {
        var product = repository.getReferenceById(data.id());
        var newCategory = data.categoryId() != null
                ? categoryRepository.getReferenceById(data.categoryId())
                : null;
        product.updateInformation(data, newCategory);
        return ResponseEntity.ok(new ProductDetailsData(product));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        var product = repository.getReferenceById(id);
        product.delete();
        product.getCategory().decrementProductCount();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity details(@PathVariable Long id) {
        var product = repository.getReferenceById(id);
        return ResponseEntity.ok(new ProductDetailsData(product));
    }

}

