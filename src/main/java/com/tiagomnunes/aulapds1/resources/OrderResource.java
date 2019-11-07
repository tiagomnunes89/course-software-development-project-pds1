package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.dto.OrderDTO;
import com.tiagomnunes.aulapds1.dto.OrderItemDTO;
import com.tiagomnunes.aulapds1.entities.Order;
import com.tiagomnunes.aulapds1.services.OrderService;
import com.tiagomnunes.aulapds1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    @Autowired
    private OrderService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> orderList = service.findAll();
        return ResponseEntity.ok().body(orderList);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/{id}/items")
    public ResponseEntity<List<OrderItemDTO>> findItems(@PathVariable Long id) {
        List<OrderItemDTO> list = service.findItems(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/myorders")
    public ResponseEntity<List<OrderDTO>> findByClient() {
        List<OrderDTO> orderList = service.findByClient();
        return ResponseEntity.ok().body(orderList);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/client/{clientId}")
    public ResponseEntity<List<OrderDTO>> findByClientId(@PathVariable Long clientId) {
        List<OrderDTO> orderList = service.findByClientId(clientId);
        return ResponseEntity.ok().body(orderList);
    }
}