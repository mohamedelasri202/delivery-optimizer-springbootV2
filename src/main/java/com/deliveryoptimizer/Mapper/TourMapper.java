package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.DTO.TourDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TourMapper {

    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "vehicle.id", target = "vehicleId")
//    @Mapping(source = "vehicle.registrationNumber", target = "vehicleRegistrationNumber")
    @Mapping(source = "vehicle.type", target = "vehicleType")
    TourDTO toDTO(Tour tour);

    @Mapping(target = "warehouse", ignore = true) // set manually in service
    @Mapping(target = "vehicle", ignore = true)   // set manually in service
    Tour toEntity(TourDTO dto);
}
