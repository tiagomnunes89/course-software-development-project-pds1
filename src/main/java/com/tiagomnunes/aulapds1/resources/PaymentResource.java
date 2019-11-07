package com.tiagomnunes.aulapds1.resources;

import com.tiagomnunes.aulapds1.dto.PaymentDTO;
import com.tiagomnunes.aulapds1.dto.PaymentDTO;
import com.tiagomnunes.aulapds1.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/payments")
public class PaymentResource {

    @Autowired
    private PaymentService service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> findAll() {
        List<PaymentDTO> PaymentList = service.findAll();
        return ResponseEntity.ok().body(PaymentList);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable Long id) {
        PaymentDTO PaymentDTO = service.findById(id);
        return ResponseEntity.ok().body(PaymentDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PaymentDTO> insert(@RequestBody PaymentDTO paymentDTO) {
        paymentDTO = service.insert(paymentDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(paymentDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(paymentDTO);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        paymentDTO = service.update(id, paymentDTO);
        return ResponseEntity.ok(paymentDTO);
    }
}