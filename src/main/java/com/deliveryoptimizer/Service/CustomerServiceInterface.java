package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerServiceInterface {

    CustomerDTO createCustomer(CustomerDTO customer);
    CustomerDTO updateCustomer(CustomerDTO customer);
    CustomerDTO deleteCustomer(Long id);

    Page<Customer> findByPreferredTimeSlot(String preferredTimeSlot, Pageable pageable);
}
