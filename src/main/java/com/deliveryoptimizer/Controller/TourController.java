package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Service.TourService;
import com.deliveryoptimizer.Service.TourServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    private final TourServiceInterface tourService;

    public TourController(TourServiceInterface tourService) {
        this.tourService = tourService;
    }

    @PostMapping
    public ResponseEntity<TourDTO> create(@RequestBody TourDTO dto) {
        return ResponseEntity.ok(tourService.create(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TourDTO> update(@PathVariable Integer id, @RequestBody TourDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(tourService.update(dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        Boolean deleted = tourService.delete(id);
        if (deleted) return ResponseEntity.ok("Tour with id " + id + " deleted successfully");
        return ResponseEntity.status(404).body("Tour not found");
    }


    @GetMapping("/{id}/optimized-deliveries")
    public ResponseEntity<OptimizedTourDTO> getOptimizedRoute(@PathVariable Integer id) {


        OptimizedTourDTO optimizedTour = tourService.getOptimizedTour(id);

        if (optimizedTour != null) {
            return ResponseEntity.ok(optimizedTour);
        } else {

            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/compareAlgorithems")

    public ResponseEntity<List<OptimizedTourDTO>> compareAlgorithems(@PathVariable Integer id) {

        List<OptimizedTourDTO> comparisonResults = tourService.compareAlgorithem(id);

        if (comparisonResults != null && !comparisonResults.isEmpty()) {

            return ResponseEntity.ok(comparisonResults);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete") // PATCH is better for partial update (status change)
    @Operation(summary = "Mark tour as COMPLETE and generate historical data",
            description = "Changes the tour status to COMPLETED and triggers the creation of DeliveryHistory records for all associated packages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tour status updated and history created"),
            @ApiResponse(responseCode = "404", description = "Tour ID not found")
    })
    public ResponseEntity<TourDTO> completeTour(@PathVariable Integer id) {


        TourDTO completedTourDTO = tourService.completeTour(id);


        return ResponseEntity.ok(completedTourDTO);
    }
}
