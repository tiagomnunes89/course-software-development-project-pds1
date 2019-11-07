package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CategoryDTO;
import com.tiagomnunes.aulapds1.dto.ProductCategoriesDTO;
import com.tiagomnunes.aulapds1.dto.ProductDTO;
import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.entities.Product;
import com.tiagomnunes.aulapds1.repositories.CategoryRepository;
import com.tiagomnunes.aulapds1.repositories.ProductRepository;
import com.tiagomnunes.aulapds1.services.exceptions.DatabaseException;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(e -> new ProductDTO(e));
    }

    public ProductDTO findById(Long id) {
        Optional<Product> user = repository.findById(id);
        Product entity = user.orElseThrow(() -> new ResourceNotFoundException(id));
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductCategoriesDTO dto) {
        Product entity = dto.toEntity();
        setProductCategories(entity, dto.getCategories());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    private void setProductCategories(Product entity, List<CategoryDTO> categories) {
        entity.getCategories().clear();
        for (CategoryDTO dto : categories) {
            Category category = categoryRepository.getOne(dto.getId());
            entity.getCategories().add(category);
        }
    }

    @Transactional
    public ProductDTO update(Long id, ProductCategoriesDTO dto) {
        try {
            Product entity = repository.getOne(id);
            updateData(entity, dto);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Product entity, ProductCategoriesDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgURL(dto.getImgURL());
        if (dto.getCategories() != null && dto.getCategories().size() > 0) {
            setProductCategories(entity, dto.getCategories());
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCategoryPaged(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.getOne(categoryId);
        Page<Product> products = repository.findByCategory(category, pageable);
        return products.map(e -> new ProductDTO(e));
    }
}