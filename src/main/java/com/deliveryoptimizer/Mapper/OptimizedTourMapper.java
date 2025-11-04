package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptimizedTourMapper {

    @Mapping(source = "tour.id", target = "tourId")
    @Mapping(source = "tour.vehicle", target = "vehicle")
    @Mapping(source = "tour.warehouse", target = "warehouse")
    @Mapping(source = "deliveries", target = "deliveries")
    @Mapping(source = "totalDistance", target = "totalDistance")
    OptimizedTourDTO toDTO(Tour tour, List<Delivery> deliveries, double totalDistance);
}
