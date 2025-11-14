package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.Model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable ;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Page<Customer> findByPreferredTimeSlot(String preferredTimeSlot, Pageable pageable);
}
