package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Mapper.CustomerMapper;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import jakarta.transaction.Transactional; // Import necessary for transactional methods
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page; // NEW: Required for Pagination
import org.springframework.data.domain.Pageable; // NEW: Required for Pagination
import org.springframework.stereotype.Service;

import java.util.Optional; // Required for handling findById
import java.util.List;

@Service
@Slf4j
public class CustomerService implements CustomerServiceInterface {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // Constructor Injection (Correct)
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerdto) {
        log.info("Starting creation of new customer.");
        Customer customer = customerMapper.toEntity(customerdto);
        Customer saved = customerRepository.save(customer);
        log.info("Customer ID {} successfully created.", saved.getId());
        return customerMapper.toDTO(saved);
    }
    @Transactional
    public CustomerDTO updateCustomer(CustomerDTO customerdto) {
        log.info("Updating Customer ID {}.", customerdto.getId());

        customerRepository.findById(customerdto.getId())
                .orElseThrow(() -> new RuntimeException("Cannot update: Customer not found with id " + customerdto.getId()));

        Customer customer = customerMapper.toEntity(customerdto);
        Customer saved = customerRepository.save(customer); // save() handles both creation and update
        log.info("Customer ID {} successfully updated.", saved.getId());
        return customerMapper.toDTO(saved);
    }
    @Transactional
    public CustomerDTO deleteCustomer(Long id) {
        log.warn("Attempting to delete Customer ID {}.", id);
        Customer customerToDelete = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
        CustomerDTO deletedDTO = customerMapper.toDTO(customerToDelete);
        customerRepository.delete(customerToDelete);
        log.info("Customer ID {} successfully deleted.", id);
        return deletedDTO;
    }
}