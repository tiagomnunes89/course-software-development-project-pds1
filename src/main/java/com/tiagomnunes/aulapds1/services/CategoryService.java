package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(Long id){
        Optional<Category> order = repository.findById(id);
        return order.get();
    }
}