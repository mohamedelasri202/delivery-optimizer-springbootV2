package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.CustomerDTO;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // --- Helper method to convert List<Delivery> to List<Long> ---
    // MapStruct automatically uses this method to map 'deliveries' to 'deliveryIds'.
    default List<Long> deliveryListToLongList(List<Delivery> deliveries) {
        if (deliveries == null) {
            return null;
        }
        return deliveries.stream()
                .map(Delivery::getId)
                .collect(Collectors.toList());
    }

    // 1. ENTITY TO DTO (Egress)
    // MapStruct detects 'deliveries' -> 'deliveryIds' automatically via the helper method.
   // Ignore this complex list if present on Customer Entity
    CustomerDTO toDTO(Customer customer);


    // 2. DTO TO ENTITY (Ingress)
    // Ignore complex entity fields that cannot be mapped from the DTO.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true) // Ignore the List<Delivery> object

    Customer toEntity(CustomerDTO customerDTO);
}