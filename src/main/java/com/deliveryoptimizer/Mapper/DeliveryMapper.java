package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface DeliveryMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "tour.id", target = "tourId")
    DeliveryDTO toDTO(Delivery delivery);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "historyRecords", ignore = true)
    Delivery toEntity(DeliveryDTO dto);
}