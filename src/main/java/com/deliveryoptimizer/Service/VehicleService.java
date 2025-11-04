package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.VehicleDTO;
import com.deliveryoptimizer.Mapper.VehicleMapper;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j; // ⭐️ Import SLF4J annotation
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VehicleService implements VehicleServiceInterface {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Transactional
    public VehicleDTO create(VehicleDTO vehicleDTO) {
        log.info("Starting creation of new vehicle with type: {}.", vehicleDTO.getType());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        Vehicle saved = vehicleRepository.save(vehicle);

        log.info("Vehicle ID {} successfully created and saved.", saved.getId());
        return vehicleMapper.toDTO(saved);
    }

    @Transactional
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        log.info("Starting update for vehicle ID {}.", vehicleDTO.getId());

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        Vehicle updated = vehicleRepository.save(vehicle);

        log.info("Vehicle ID {} successfully updated.", updated.getId());
        return vehicleMapper.toDTO(updated);
    }

    @Transactional
    public Boolean delete(int id) {
        log.warn("Attempting to delete vehicle ID {}.", id);

        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicleRepository.delete(vehicle);
                    log.info("Vehicle ID {} successfully deleted.", id);
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Deletion failed: Vehicle ID {} not found.", id);
                    return false;
                });
    }

    public List<VehicleDTO> getAll() {
        log.debug("Fetching all vehicles from repository.");
        return vehicleMapper.toDTOList(vehicleRepository.findAll());
    }
}