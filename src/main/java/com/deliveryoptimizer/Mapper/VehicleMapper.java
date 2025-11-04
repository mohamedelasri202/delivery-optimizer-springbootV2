package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.VehicleDTO;
import com.deliveryoptimizer.Model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mappings({
            @Mapping(target = "tourIds", expression = "java(vehicle.getTours() != null ? vehicle.getTours().stream().map(t -> t.getId()).toList() : null)")
    })
    VehicleDTO toDTO(Vehicle vehicle);

    @Mappings({
            @Mapping(target = "tours", ignore = true)
    })
    Vehicle toEntity(VehicleDTO dto);

    List<VehicleDTO> toDTOList(List<Vehicle> vehicles);
}
