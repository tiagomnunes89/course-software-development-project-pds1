package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.services.CategoryService;
import com.tiagomnunes.aulapds1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        List<Category> categoryList = service.findAll();
        return ResponseEntity.ok().body(categoryList);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<Category> findById(@PathVariable  Long id){
        Category category = service.findById(id);
        return ResponseEntity.ok().body(category);
    }
}
