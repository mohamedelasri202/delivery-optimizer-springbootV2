package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Mapper.OptimizedTourMapper;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.Optimizer.RouteOptimizationContext; // New Import
import com.deliveryoptimizer.Optimizer.TourOptimizer;
import com.deliveryoptimizer.Repositories.TourRepository;
import com.deliveryoptimizer.Mapper.TourMapper;
import com.deliveryoptimizer.Repositories.VehicleRepository;
import com.deliveryoptimizer.Repositories.WareHouseRepository;
import com.deliveryoptimizer.util.DistanceCalculator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup; // CRITICAL Import
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public abstract class TourService implements TourServiceInterface {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final WareHouseRepository wareHouseRepository;
    private final VehicleRepository vehicleRepository;
    private final List<TourOptimizer> optimizers;
    private final TourOptimizer activeOptimizer;
    private final OptimizedTourMapper optimizedTourMapper;

    @Lookup
    public abstract RouteOptimizationContext getRouteOptimizationContext();



    public TourService(TourRepository tourRepository,
                       TourMapper tourMapper,
                       WareHouseRepository wareHouseRepository,
                       VehicleRepository vehicleRepository,
                       List<TourOptimizer> optimizers,
                       @Qualifier("tourOptimizer") TourOptimizer activeOptimizer,
                       OptimizedTourMapper optimizedTourMapper) {
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
        this.wareHouseRepository = wareHouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.optimizers = optimizers;
        this.activeOptimizer = activeOptimizer;
        this.optimizedTourMapper = optimizedTourMapper;
    }



    @Transactional
    public TourDTO create(TourDTO dto) {
        log.info("Starting creation of new Tour.");
        Tour tour = tourMapper.toEntity(dto);

        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = wareHouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> new RuntimeException("Warehouse not found with id " + dto.getWarehouseId()));
            tour.setWarehouse(warehouse);
        }
        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId().intValue())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found with id " + dto.getVehicleId()));
            tour.setVehicle(vehicle);
        }

        Tour saved = tourRepository.save(tour);
        log.info("Successfully created and saved Tour ID {}.", saved.getId());
        return tourMapper.toDTO(saved);
    }

    @Transactional
    public TourDTO update(TourDTO dto) {
        log.info("Updating Tour ID {}.", dto.getId());
        Tour tour = tourMapper.toEntity(dto);
        Tour updatedTour = tourRepository.save(tour);
        log.info("Tour ID {} successfully updated.", updatedTour.getId());
        return tourMapper.toDTO(updatedTour);
    }

    @Transactional
    public Boolean delete(Integer id) {
        log.warn("Attempting to delete Tour ID {}.", id);
        return tourRepository.findById(id).map(tour -> {
            tourRepository.delete(tour);
            log.info("Tour ID {} successfully deleted.", id);
            return true;
        }).orElseGet(() -> {
            log.info("Deletion failed: Tour ID {} not found.", id);
            return false;
        });
    }

    public TourDTO getById(Integer id) {
        log.debug("Fetching Tour ID {} details.", id);
        return tourRepository.findById(id)
                .map(tourMapper::toDTO)
                .orElse(null);
    }



    @Override
    public OptimizedTourDTO getOptimizedTour(Integer id) {
        log.info("Starting single optimization for Tour ID {}.", id);

        Tour tour = tourRepository.findByIdWithDetails(id).orElse(null);
        if (tour == null) {
            log.warn("Optimization failed: Tour ID {} not found.", id);
            return null;
        }


        RouteOptimizationContext context = getRouteOptimizationContext();

        context.setTourId((long) tour.getId());
        context.setVehicle(tour.getVehicle());
        context.setWarehouse(tour.getWarehouse());
        context.setDeliveries(tour.getDeliveries());

        log.info("Selected optimizer: {}. Starting calculation.", activeOptimizer.getClass().getSimpleName());

        List<Delivery> optimizedDeliveries = activeOptimizer.calculateOptimalTour(context);

        double totalDistance = getTotallDistance(tour.getWarehouse(), optimizedDeliveries);
        log.info("Optimization complete for Tour ID {}. Total distance: {} km. Optimizer: {}",
                id, totalDistance, activeOptimizer.getClass().getSimpleName());


        return optimizedTourMapper.toDTO(tour, optimizedDeliveries, totalDistance);
    }


    public double getTotallDistance(Warehouse warehouse, List<Delivery> deliveries) {
        double totalDistance = 0;
        double currentLat = warehouse.getLatitude();
        double currentLon = warehouse.getLongitude();

        for(Delivery d : deliveries) {
            totalDistance += DistanceCalculator.DistanceCalc(currentLon, currentLat, d.getLongitude(), d.getLatitude());
            currentLat = d.getLatitude();
            currentLon = d.getLongitude();
        }

        totalDistance += DistanceCalculator.DistanceCalc(currentLon, currentLat, warehouse.getLongitude(), warehouse.getLatitude());
        return totalDistance;
    }


    @Override
    public List<OptimizedTourDTO> compareAlgorithem(Integer id) {
        log.info("Starting comparison of all {} active algorithms for Tour ID {}.", optimizers.size(), id);

        Optional<Tour> tourOptional = tourRepository.findByIdWithDetails(id);
        if (tourOptional.isEmpty()) {
            log.warn("Comparison failed: Tour ID {} not found.", id);
            return null;
        }
        Tour tour = tourOptional.get();

        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) {
            log.warn("Tour ID {} has no deliveries to compare.", id);
            return List.of();
        }

        List<OptimizedTourDTO> comparisonResults = new ArrayList<>();


        RouteOptimizationContext context = getRouteOptimizationContext();

        for (TourOptimizer optimizer : optimizers) {
            String optimizerName = optimizer.getClass().getSimpleName();
            log.info("Executing {} optimization.", optimizerName);

            // Populate context for the current run
            context.setTourId((long) tour.getId());
            context.setVehicle(tour.getVehicle());
            context.setWarehouse(tour.getWarehouse());
            context.setDeliveries(tour.getDeliveries());


            List<Delivery> optimizedDeliveries = optimizer.calculateOptimalTour(context);
            double distance = getTotallDistance(tour.getWarehouse(), optimizedDeliveries);

            log.info("{} distance for Tour ID {}: {} km.", optimizerName, id, distance);

            OptimizedTourDTO result = optimizedTourMapper.toDTO(tour, optimizedDeliveries, distance);
            result.setAlgorithmUsed(optimizerName);
            comparisonResults.add(result);
        }

        return comparisonResults;
    }
}