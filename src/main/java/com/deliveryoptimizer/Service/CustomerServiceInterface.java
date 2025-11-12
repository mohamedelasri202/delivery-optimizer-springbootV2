package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Model.Customer;

import java.util.List;

public interface CustomerServiceInterface {

    CustomerDTO createCustomer(CustomerDTO customer);
    CustomerDTO updateCustomer(CustomerDTO customer);
    CustomerDTO deleteCustomer(Long id);
}
