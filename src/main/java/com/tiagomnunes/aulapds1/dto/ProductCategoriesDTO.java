package com.tiagomnunes.aulapds1.dto;

import com.tiagomnunes.aulapds1.entities.Product;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoriesDTO implements Serializable {

    @NotEmpty (message = "Can´t be empty")
    @Length(min = 3, max = 15, message = "Length must be between 2 and 80")
    private String name;
    @NotEmpty (message = "Can´t be empty")
    @Length(min = 8, message = "Length must be at least 8")
    private String description;
    @Positive (message = "Can't be a negative value")
    private Double price;
    private String imgURL;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductCategoriesDTO() {
    }

    public ProductCategoriesDTO(String name, String description, Double price, String imgURL) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
    }

    public ProductCategoriesDTO(Product entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgURL = entity.getImgURL();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public Product toEntity() {
        return new Product(null, name, description, price, imgURL);
    }
}
