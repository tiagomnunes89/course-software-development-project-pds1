package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CategoryDTO;
import com.tiagomnunes.aulapds1.dto.PaymentDTO;
import com.tiagomnunes.aulapds1.entities.Category;
import com.tiagomnunes.aulapds1.entities.Payment;
import com.tiagomnunes.aulapds1.repositories.PaymentRepository;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    public List<PaymentDTO> findAll() {
        List<Payment> list = paymentRepository.findAll();
        return list.stream().map(e -> new PaymentDTO(e)).collect(Collectors.toList());
    }

    public PaymentDTO findById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        Payment entity = payment.orElseThrow(() -> new ResourceNotFoundException(id));
        return new PaymentDTO(entity);
    }
}
