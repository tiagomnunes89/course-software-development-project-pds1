package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CategoryDTO;
import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.repositories.CategoryRepository;
import com.tiagomnunes.aulapds1.services.exceptions.DatabaseException;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
    }

    public CategoryDTO findById(Long id) {
        Optional<Category> category = repository.findById(id);
        Category entity = category.orElseThrow(() -> new ResourceNotFoundException(id));
        return new CategoryDTO(entity);
    }

    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = categoryDTO.toEntity();
        entity = repository.save(entity);
        return new CategoryDTO(entity);
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

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category entity = repository.getOne(id);
            updateData(entity, categoryDTO);
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Category entity, CategoryDTO categoryDTO) {
        entity.setName(categoryDTO.getName());
    }
}