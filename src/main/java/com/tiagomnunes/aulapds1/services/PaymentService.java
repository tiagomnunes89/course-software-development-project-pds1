package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.PaymentDTO;
import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.entities.Payment;
import com.tiagomnunes.aulapds1.repositories.OrderRepository;
import com.tiagomnunes.aulapds1.repositories.PaymentRepository;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<PaymentDTO> findAll() {
        List<Payment> list = paymentRepository.findAll();
        return list.stream().map(e -> new PaymentDTO(e)).collect(Collectors.toList());
    }

    public PaymentDTO findById(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        Payment entity = payment.orElseThrow(() -> new ResourceNotFoundException(id));
        return new PaymentDTO(entity);
    }

    @Transactional
    public PaymentDTO insert(PaymentDTO paymentDTO) {
        Order order = orderRepository.getOne(paymentDTO.getOrderId());
        Payment payment = new Payment(null, paymentDTO.getMoment(), order);
        order.setPayment(payment);
        orderRepository.save(order);
        return new PaymentDTO(order.getPayment());
    }

    @Transactional
    public PaymentDTO update(Long id, PaymentDTO paymentDTO) {
        try {
            Payment entity = paymentRepository.getOne(id);
            updateData(entity, paymentDTO);
            entity = paymentRepository.save(entity);
            return new PaymentDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Payment entity, PaymentDTO paymentDTO) {
        entity.setMoment(paymentDTO.getMoment());
    }
}
