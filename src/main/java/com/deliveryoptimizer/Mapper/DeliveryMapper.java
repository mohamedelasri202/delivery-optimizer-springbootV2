package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Model.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(source = "tour.id", target = "tourId")
    DeliveryDTO toDTO(Delivery delivery);

    @Mapping(source = "tourId", target = "tour.id")
    Delivery toEntity(DeliveryDTO dto);
}
