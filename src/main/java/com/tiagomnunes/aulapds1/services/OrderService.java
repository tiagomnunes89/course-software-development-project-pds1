package com.tiagomnunes.aulapds1.services;

import com.tiagomnunes.aulapds1.dto.CategoryDTO;
import com.tiagomnunes.aulapds1.dto.OrderDTO;
import com.tiagomnunes.aulapds1.dto.OrderItemDTO;
import com.tiagomnunes.aulapds1.entities.*;
import com.tiagomnunes.aulapds1.entities.enums.OrderStatus;
import com.tiagomnunes.aulapds1.repositories.OrderItemRepository;
import com.tiagomnunes.aulapds1.repositories.OrderRepository;
import com.tiagomnunes.aulapds1.repositories.ProductRepository;
import com.tiagomnunes.aulapds1.repositories.UserRepository;
import com.tiagomnunes.aulapds1.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<OrderDTO> findAll() {
        List<Order> list = orderRepository.findAll();
        return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
    }

    public OrderDTO findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        Order entity = order.orElseThrow(() -> new ResourceNotFoundException(id));
        authService.validateOwnOrderOrAdmin(entity);
        return new OrderDTO(entity);
    }

    public List<OrderDTO> findByClient() {
        User client = authService.authenticated();
        List<Order> list = orderRepository.findByClient(client);
        return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderItemDTO> findItems(Long id) {
        Order order = orderRepository.getOne(id);
        authService.validateOwnOrderOrAdmin(order);
        Set<OrderItem> set = order.getItems();
        return set.stream().map(e -> new OrderItemDTO(e)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findByClientId(Long clientId) {
        User client = userRepository.getOne(clientId);
        List<Order> list = orderRepository.findByClient(client);
        return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO placeOrder(List<OrderItemDTO> dtoList) {
        User client = authService.authenticated();
        Order order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, client);
        for (OrderItemDTO itemDTO : dtoList) {
            Product product = productRepository.getOne(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), itemDTO.getPrice());
            order.getItems().add(item);
        }
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO update(Long id, OrderDTO orderDTO) {
        try {
            Order entity = orderRepository.getOne(id);
            updateData(entity, orderDTO);
            entity = orderRepository.save(entity);
            return new OrderDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Order entity, OrderDTO orderDTO) {
        entity.setOrderStatus(orderDTO.getOrderStatus());
    }
}