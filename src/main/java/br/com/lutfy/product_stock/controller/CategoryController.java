package br.com.lutfy.product_stock.controller;

import br.com.lutfy.product_stock.domain.category.*;
import br.com.lutfy.product_stock.domain.category.*;
import br.com.lutfy.product_stock.domain.product.ProductRepository;
import br.com.lutfy.product_stock.infra.ErrorHandler;
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
@RequestMapping("categorias")
public class CategoryController {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public ResponseEntity register(@RequestBody @Valid CategoryRegistrationData data, UriComponentsBuilder uriBuilder) {
        var category = new Category(data);
        repository.save(category);
        var uri = uriBuilder.path("/categorias/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(new CategoryDetailsData(category));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryListingData>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pagination) {
        var page = repository.findAllByActiveTrue(pagination).map(CategoryListingData::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid CategoryUpdateData data) {
        var category = repository.getReferenceById(data.id());
        category.updateInformation(data);
        return ResponseEntity.ok(new CategoryDetailsData(category));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        boolean hasLinkedProduct = productRepository.existsByCategoryIdAndActiveTrue(id);
        if (hasLinkedProduct) {
            throw new ErrorHandler.ValidationException("Não é possível excluir uma categoria com produtos vinculados.");
        }
        var category = repository.getReferenceById(id);
        category.delete();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity details(@PathVariable Long id) {
        var category = repository.getReferenceById(id);
        return ResponseEntity.ok(new CategoryDetailsData(category));
    }
}