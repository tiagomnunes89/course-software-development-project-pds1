package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository <Order, Long> {

    List<Order> findByClient (User client);
}
