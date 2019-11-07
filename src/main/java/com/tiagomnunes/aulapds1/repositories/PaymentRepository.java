package com.tiagomnunes.aulapds1.repositories;

import com.tiagomnunes.aulapds1.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <Payment, Long> {

}
