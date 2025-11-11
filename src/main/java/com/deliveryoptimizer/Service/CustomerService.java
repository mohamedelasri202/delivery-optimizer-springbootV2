package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Mapper.CustomerMapper;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerService implements CustomerServiceInterface {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
        public CustomerDTO createCustomer(CustomerDTO customerdto) {

        Customer customer =customerMapper.toEntity(customerdto);
        Customer saved = customerRepository.save(customer);
        log.info("created succefully: {}", saved.getId());
        return customerMapper.toDTO(saved);

        }
}
