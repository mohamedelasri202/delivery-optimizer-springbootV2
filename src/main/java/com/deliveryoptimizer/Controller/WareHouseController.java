package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.WarehouseDTO;
import com.deliveryoptimizer.Mapper.WarehouseMapper;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.Service.WareHouseServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/warehouse")
public class WareHouseController {

    private final WareHouseServiceInterface wareHouseService;
    private final WarehouseMapper warehouseMapper;

    public WareHouseController(WareHouseServiceInterface wareHouseService, WarehouseMapper warehouseMapper) {
        this.wareHouseService = wareHouseService;
        this.warehouseMapper = warehouseMapper;
    }


    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Warehouse created = wareHouseService.createWarehouse(warehouse);
        WarehouseDTO responseDTO = warehouseMapper.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@PathVariable int id, @RequestBody WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse.setId(id);
        Warehouse updated = wareHouseService.updateWarehouse(id, warehouse);
        WarehouseDTO responseDTO = warehouseMapper.toDTO(updated);
        return ResponseEntity.ok(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable int id) {
        Boolean deleted = wareHouseService.deleteWarehouse(id);
        if (deleted) {
            return ResponseEntity.ok("Warehouse with id " + id + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Warehouse with id " + id + " not found");
        }
    }
}
