package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.Mapper.WarehouseMapper;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.Repositories.WareHouseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j; // ⭐️ Import SLF4J annotation
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WareHouseService implements WareHouseServiceInterface {

    private final WareHouseRepository wareHouseRepository;
    private final WarehouseMapper warehouseMapper;


    public WareHouseService(WareHouseRepository wareHouseRepository, WarehouseMapper warehouseMapper) {
        this.wareHouseRepository = wareHouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Override
    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        log.info("Starting creation of new warehouse: {}.", warehouse.getName());
        Warehouse savedWarehouse = wareHouseRepository.save(warehouse);
        log.info("Warehouse ID {} successfully created.", savedWarehouse.getId());
        return savedWarehouse;
    }

    @Override
    @Transactional
    public Warehouse updateWarehouse(int id, Warehouse warehouse) {
        log.info("Starting update for Warehouse ID {}.", id);

        Optional<Warehouse> existing = wareHouseRepository.findById(id);

        if (existing.isPresent()) {
            Warehouse updatedWarehouse = existing.get();

            log.debug("Updating warehouse ID {}. Name: {} -> {}.",
                    id, updatedWarehouse.getName(), warehouse.getName());

            updatedWarehouse.setName(warehouse.getName());
            updatedWarehouse.setLatitude(warehouse.getLatitude());
            updatedWarehouse.setLongitude(warehouse.getLongitude());

            Warehouse savedWarehouse = wareHouseRepository.save(updatedWarehouse);
            log.info("Warehouse ID {} successfully updated.", id);
            return savedWarehouse;
        }

        log.warn("Update failed: Warehouse ID {} not found.", id);
        return null;
    }

    @Override
    @Transactional
    public Boolean deleteWarehouse(int id) {
        log.warn("Attempting to delete Warehouse ID {}.", id);

        return wareHouseRepository.findById(id)
                .map(warehouse -> {
                    wareHouseRepository.delete(warehouse);
                    log.info("Warehouse ID {} successfully deleted.", id);
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Deletion failed: Warehouse ID {} not found.", id);
                    return false;
                });
    }

}