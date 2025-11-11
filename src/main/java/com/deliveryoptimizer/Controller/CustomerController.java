package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Service.CustomerService;
import com.deliveryoptimizer.Service.CustomerServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class CustomerController {
    private final CustomerServiceInterface customerService;
    public CustomerController(CustomerServiceInterface customerService) {
        this.customerService = customerService;
    }
    public ResponseEntity<Customer>createCustomer(Customer customer){

    }

}
