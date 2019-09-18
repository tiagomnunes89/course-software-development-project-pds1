package com.tiagomnunes.aulapds1.dto;

import com.tiagomnunes.aulapds1.entities.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

    private Long id;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, String description, Double price, String imgURL) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category toEntity() {
        return new Category(id, name);
    }
}