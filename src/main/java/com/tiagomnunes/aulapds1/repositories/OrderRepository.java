package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <Order, Long> {

}
