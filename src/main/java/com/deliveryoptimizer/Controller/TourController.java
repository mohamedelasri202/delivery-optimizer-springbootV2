package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Service.TourService;
import com.deliveryoptimizer.Service.TourServiceInterface;
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


    @PostMapping("/{id}/optimize")
    public ResponseEntity<OptimizedTourDTO> optimize(@PathVariable Integer id) {

        OptimizedTourDTO optimizedTour = tourService.getOptimizedTour(id);
        if(optimizedTour != null){
            return ResponseEntity.ok(optimizedTour);
        }else {
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
}
