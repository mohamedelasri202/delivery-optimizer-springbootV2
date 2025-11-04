package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.Optimizer.TourOptimizer;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TourServiceInterface {
    TourDTO create(TourDTO dto);
    TourDTO update(TourDTO dto);
    Boolean delete(Integer id);
//    List<TourDTO> getAll();
    TourDTO getById(Integer id);

     OptimizedTourDTO getOptimizedTour(Integer id);

     double getTotallDistance(Warehouse warehouse , List<Delivery> orderedDeliveries);
    List<OptimizedTourDTO> compareAlgorithem(Integer id);
}
