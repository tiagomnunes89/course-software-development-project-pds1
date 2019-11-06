package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.OrderDTO;
import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.entities.User;
import com.tiagomnunes.aulapds1.repositories.OrderRepository;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    AuthService authService;



    public List<OrderDTO> findAll() {
        List<Order> list = repository.findAll();
        return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
    }

    public OrderDTO findById(Long id) {
        Optional<Order> order = repository.findById(id);
        Order entity = order.orElseThrow(() -> new ResourceNotFoundException(id));
        authService.validateOwnOrderOrAdmin(entity);
        return new OrderDTO(entity);
    }

    public List<OrderDTO> findByClient () {
        User client = authService.authenticated();
        List<Order> list = repository.findByClient(client);
        return list.stream().map(e-> new OrderDTO(e)).collect(Collectors.toList());
    }
}