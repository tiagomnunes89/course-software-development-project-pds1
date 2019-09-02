package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category, Long> {

}
