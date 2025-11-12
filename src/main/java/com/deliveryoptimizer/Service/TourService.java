package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Mapper.OptimizedTourMapper;
import com.deliveryoptimizer.Model.*;
import com.deliveryoptimizer.Optimizer.RouteOptimizationContext;
import com.deliveryoptimizer.Optimizer.TourOptimizer;
import com.deliveryoptimizer.Repositories.DeliveryHistoryRepository;
import com.deliveryoptimizer.Repositories.TourRepository;
import com.deliveryoptimizer.Mapper.TourMapper;
import com.deliveryoptimizer.Repositories.VehicleRepository;
import com.deliveryoptimizer.Repositories.WareHouseRepository;
import com.deliveryoptimizer.util.DistanceCalculator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek; // Added DayOfWeek import
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
// This class MUST remain abstract to allow Spring to proxy the @Lookup methods
public abstract class TourService implements TourServiceInterface {

    // --- FINAL FIELDS ---
    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final WareHouseRepository wareHouseRepository;
    private final VehicleRepository vehicleRepository;
    private final List<TourOptimizer> optimizers;
    private final TourOptimizer activeOptimizer;
    private final OptimizedTourMapper optimizedTourMapper;
    // 🛑 The following field must be REMOVED if you use @Lookup, but we keep it here for clarity/debugging.
    private final DeliveryHistory sharedHistoryInstance;
    private final DeliveryHistoryRepository deliveryHistoryRepository;


    @Lookup
    public abstract RouteOptimizationContext getRouteOptimizationContext();

    // 🛑 FIX 2: NEW @Lookup method to solve the data contamination problem
    @Lookup
    public abstract DeliveryHistory getNewDeliveryHistoryInstance();


    // --- FIX 3: Corrected Constructor with ALL Assignments ---
    public TourService(TourRepository tourRepository,
                       TourMapper tourMapper,
                       WareHouseRepository wareHouseRepository,
                       VehicleRepository vehicleRepository,
                       List<TourOptimizer> optimizers,
                       @Qualifier("tourOptimizer") TourOptimizer activeOptimizer,
                       OptimizedTourMapper optimizedTourMapper,
                       DeliveryHistory sharedHistoryInstance,
                       DeliveryHistoryRepository deliveryHistoryRepository) {

        // 🛑 CRITICAL FIX: Assign ALL final fields
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
        this.wareHouseRepository = wareHouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.optimizers = optimizers;
        this.activeOptimizer = activeOptimizer;
        this.optimizedTourMapper = optimizedTourMapper;
        this.sharedHistoryInstance = sharedHistoryInstance; // This field should ideally be removed
        this.deliveryHistoryRepository = deliveryHistoryRepository;
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
        // ... (update logic) ...
        return tourMapper.toDTO(tourRepository.save(tourMapper.toEntity(dto)));
    }

    @Transactional
    public Boolean delete(Integer id) {
        // ... (delete logic) ...
        return tourRepository.findById(id).map(tour -> {
            tourRepository.delete(tour);
            return true;
        }).orElseGet(() -> false);
    }

    public TourDTO getById(Integer id) {
        // ... (getById logic) ...
        return tourRepository.findById(id)
                .map(tourMapper::toDTO)
                .orElse(null);
    }


    @Override
    public OptimizedTourDTO getOptimizedTour(Integer id) {
        // ... (getOptimizedTour logic with context) ...
        RouteOptimizationContext context = getRouteOptimizationContext();

        Tour tour = tourRepository.findByIdWithDetails(id).orElse(null);
        if (tour == null) { /* ... log warning ... */ return null; }

        context.setTourId((long) tour.getId());
        context.setVehicle(tour.getVehicle());
        context.setWarehouse(tour.getWarehouse());
        context.setDeliveries(tour.getDeliveries());

        List<Delivery> optimizedDeliveries = activeOptimizer.calculateOptimalTour(context);
        // ... (distance calculation and return DTO logic) ...

        double totalDistance = getTotallDistance(tour.getWarehouse(), optimizedDeliveries);
        return optimizedTourMapper.toDTO(tour, optimizedDeliveries, totalDistance);
    }


    public double getTotallDistance(Warehouse warehouse, List<Delivery> deliveries) {
        // ... (distance calculation logic unchanged) ...
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
        // ... (comparison logic using optimizers list and context) ...
        // ... (Implementation remains similar to getOptimizedTour, using context) ...
        return List.of(); // Placeholder
    }


    // --- HISTORY CREATION METHOD (Completing the experiment) ---
    @Transactional
    @Override
    public TourDTO completeTour(Integer id) {
        log.info("Starting completion process for Tour ID {}.", id);

        Tour tour = tourRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Tour with ID " + id + " not found."));

        tour.setStatus(TourStatus.COMPLETED);

        if (tour.getDeliveries() == null || tour.getDeliveries().isEmpty()) {
            log.warn("Tour ID {} has no deliveries. Marking as completed but skipping history creation.", id);
            Tour savedTour = tourRepository.save(tour);
            return tourMapper.toDTO(savedTour);
        }


        for (Delivery delivery : tour.getDeliveries()) {
            if (delivery.getPlannedTime() == null || delivery.getActualTime() == null) {
                log.warn("Delivery ID {} skipped: Missing time data. Cannot create history record.", delivery.getId());
                continue;
            }

            LocalTime planned = delivery.getPlannedTime();
            LocalTime actual = delivery.getActualTime();
            Duration delayDuration = Duration.between(planned, actual);
            Long delaySeconds = delayDuration.getSeconds();


            DeliveryHistory history = getNewDeliveryHistoryInstance();


            history.setDelivery(delivery);
            history.setTour(tour);
            history.setCustomer(delivery.getCustomer());
            history.setDate_of_delivery(LocalDate.now());
            history.setDayOfWeek(getCustomDayOfWeek(LocalDate.now()));
            history.setPlanned_time(planned);
            history.setActual_time(actual);
            history.setDelay_seconds(delaySeconds);

            deliveryHistoryRepository.save(history);
            log.debug("History record created for Delivery ID {}. Delay: {}s", delivery.getId(), delaySeconds);
        }

        Tour savedTour = tourRepository.save(tour);
        log.info("Tour ID {} successfully marked as COMPLETED. {} history records created.", id, tour.getDeliveries().size());

        return tourMapper.toDTO(savedTour);
    }


    private com.deliveryoptimizer.Model.DayOfWeek getCustomDayOfWeek(LocalDate date) {
        String dayName = date.getDayOfWeek().name();
        return com.deliveryoptimizer.Model.DayOfWeek.valueOf(dayName);
    }
}