package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Service.DeleveryServiceInrerface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/delivery")
@Tag(name = "Delivery Management", description = "CRUD operations for individual deliveries, including capacity validation.")
public class DeliveryController {

    private final DeleveryServiceInrerface deliveryService;

    public DeliveryController(DeleveryServiceInrerface deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    @Operation(summary = "Create a new delivery",
            description = "Creates a new delivery and validates its load against the assigned vehicle's capacity (Weight, Volume, Max Stops).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery successfully created"),
            @ApiResponse(responseCode = "400", description = "Capacity validation failed (e.g., Weight limit exceeded)")
    })
    public ResponseEntity<DeliveryDTO> create(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO created = deliveryService.create(deliveryDTO);
        return ResponseEntity.created(URI.create("/api/delivery/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing delivery",
            description = "Updates the details of a delivery by ID.")
    public ResponseEntity<Delivery> update(@PathVariable long id, @RequestBody Delivery delivery) {
        delivery.setId(id);
        Delivery updated = deliveryService.update(delivery);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a delivery",
            description = "Deletes a delivery by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delivery successfully deleted (No Content)"),
            @ApiResponse(responseCode = "404", description = "Delivery not found")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deliver")
    @Operation(summary = "Set actual time and status to DELIVERED",
            description = "Records the actual delivery time and changes status to DELIVERED.")
    public ResponseEntity<DeliveryDTO> deliverDelivery(@PathVariable Integer id) {
        DeliveryDTO deliveredDTO = deliveryService.deliverDelivery(id);
        return ResponseEntity.ok(deliveredDTO);
    }
}