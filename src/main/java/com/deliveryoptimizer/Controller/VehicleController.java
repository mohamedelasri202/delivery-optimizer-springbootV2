package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.VehicleDTO;
import com.deliveryoptimizer.Service.VehicleServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleServiceInterface vehicleService;

    public VehicleController(VehicleServiceInterface vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.create(vehicleDTO);
        return ResponseEntity.created(URI.create("/api/vehicles/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        vehicleDTO.setId(id);
        VehicleDTO updated = vehicleService.update(vehicleDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Boolean deleted = vehicleService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Vehicle with id " + id + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }
}
