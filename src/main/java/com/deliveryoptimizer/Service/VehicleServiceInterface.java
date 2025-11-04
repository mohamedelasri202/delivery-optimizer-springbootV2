package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.VehicleDTO;
import java.util.List;

public interface VehicleServiceInterface {
    VehicleDTO create(VehicleDTO vehicleDTO);
    VehicleDTO update(VehicleDTO vehicleDTO);
    Boolean delete(int id);
    List<VehicleDTO> getAll();
}
