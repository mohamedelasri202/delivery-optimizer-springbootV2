package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Service.CustomerServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerServiceInterface customerService;

    public CustomerController(CustomerServiceInterface customerService) {
        this.customerService = customerService;
    }
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO created = customerService.createCustomer(customerDTO);

        return ResponseEntity.created(URI.create("/api/customers/" + created.getId())).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable Long id) {

        customerDTO.setId(id);
        CustomerDTO updated = customerService.updateCustomer(customerDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build(); // Returns 204 No Content
    }



}