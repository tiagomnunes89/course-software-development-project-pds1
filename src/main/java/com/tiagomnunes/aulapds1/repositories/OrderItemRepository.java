package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.OrderItem;
import com.tiagomnunes.aulapds1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository <OrderItem, Long> {

}
