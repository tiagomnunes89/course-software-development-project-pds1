package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository <Product, Long> {

}
