package com.deliveryoptimizer.Mapper;

import com.deliveryoptimizer.DTO.WarehouseDTO;
import com.deliveryoptimizer.Model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mappings({
            @Mapping(target = "tourIds", expression = "java(warehouse.getTours() != null ? warehouse.getTours().stream().map(t -> t.getId()).toList() : null)")
    })
    WarehouseDTO toDTO(Warehouse warehouse);

    @Mappings({
            @Mapping(target = "tours", ignore = true)
    })
    Warehouse toEntity(WarehouseDTO dto);

    List<WarehouseDTO> toDTOList(List<Warehouse> warehouses);
}
