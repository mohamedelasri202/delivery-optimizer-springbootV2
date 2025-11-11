package com.deliveryoptimizer.Mapper;


import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Model.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
        CustomerDTO toDTO(Customer customer);
        Customer toEntity(CustomerDTO customerDTO);

}
