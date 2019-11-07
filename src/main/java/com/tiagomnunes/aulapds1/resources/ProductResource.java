package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.dto.CategoryDTO;
import com.tiagomnunes.aulapds1.dto.ProductCategoriesDTO;
import com.tiagomnunes.aulapds1.dto.ProductDTO;
import com.tiagomnunes.aulapds1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllPaged(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "categories", defaultValue = "") String categories,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDTO> productList = service.findByNameCategoryPaged(name, categories, pageRequest);
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> findByCategoryPaged(
            @PathVariable Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDTO> productList = service.findByCategoryPaged(categoryId, pageRequest);
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO userDTO = service.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductCategoriesDTO dto) {
        ProductDTO newDTO = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductCategoriesDTO dto) {
        ProductDTO newDTO = service.update(id, dto);
        return ResponseEntity.ok(newDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}/addcategory")
    public ResponseEntity<Void> addCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        service.addCategory(id, categoryDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}/removecategory")
    public ResponseEntity<Void> removeCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        service.removeCategory(id, categoryDTO);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}/setcategories")
    public ResponseEntity<Void> setCategories(@PathVariable Long id, @RequestBody List<CategoryDTO> categoryDTOList) {
        service.setCategories(id, categoryDTOList);
        return ResponseEntity.noContent().build();
    }
}