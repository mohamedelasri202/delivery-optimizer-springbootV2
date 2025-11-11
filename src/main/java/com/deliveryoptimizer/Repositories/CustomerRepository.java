package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
