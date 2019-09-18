package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.dto.ProductDTO;
import com.tiagomnunes.aulapds1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        List<ProductDTO> productList = service.findAll();
        return ResponseEntity.ok().body(productList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO userDTO = service.findById(id);
        return ResponseEntity.ok().body(userDTO);
    }
}