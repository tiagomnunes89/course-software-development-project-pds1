package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.entities.Product;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.repositories.ProductRepository;
import com.tiagomnunes.aulapds1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id){
        Optional<Product> user = repository.findById(id);
        return user.get();
    }
}