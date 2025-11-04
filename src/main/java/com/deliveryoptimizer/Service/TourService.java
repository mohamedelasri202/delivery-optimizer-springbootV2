package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.OptimizedTourDTO;
import com.deliveryoptimizer.DTO.TourDTO;
import com.deliveryoptimizer.Mapper.OptimizedTourMapper;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.Optimizer.TourOptimizer;
import com.deliveryoptimizer.Repositories.TourRepository;
import com.deliveryoptimizer.Mapper.TourMapper;
import com.deliveryoptimizer.Repositories.VehicleRepository;
import com.deliveryoptimizer.Repositories.WareHouseRepository;
import com.deliveryoptimizer.util.DistanceCalculator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TourService implements TourServiceInterface {

    private final TourRepository tourRepository;
    private final TourMapper tourMapper;
    private final WareHouseRepository wareHouseRepository;
    private final VehicleRepository vehicleRepository;
    private final TourOptimizer nearestNeighborOptimizer;
    private final TourOptimizer clarkeWrightOptimizer;
    private final OptimizedTourMapper optimizedTourMapper;

    public TourService(TourRepository tourRepository,
                       TourMapper tourMapper,
                       WareHouseRepository wareHouseRepository,
                       VehicleRepository vehicleRepository,
                       TourOptimizer nearestNeighborOptimizer,
                       TourOptimizer clarkeWrightOptimizer,
                       OptimizedTourMapper optimizedTourMapper) {
        this.tourRepository = tourRepository;
        this.tourMapper = tourMapper;
        this.wareHouseRepository = wareHouseRepository;
        this.vehicleRepository = vehicleRepository;
        this.nearestNeighborOptimizer = nearestNeighborOptimizer;
        this.clarkeWrightOptimizer = clarkeWrightOptimizer;
        this.optimizedTourMapper = optimizedTourMapper;
    }


    @Transactional
    public TourDTO create(TourDTO dto) {
        log.info("Starting creation of new Tour.");
        Tour tour = tourMapper.toEntity(dto);


        if (dto.getWarehouseId() != null) {
            Warehouse warehouse = wareHouseRepository.findById(dto.getWarehouseId())
                    .orElseThrow(() -> {
                        log.error("Warehouse not found with id {}", dto.getWarehouseId());
                        return new RuntimeException("Warehouse not found with id " + dto.getWarehouseId());
                    });
            tour.setWarehouse(warehouse);
        }

        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId().intValue())
                    .orElseThrow(() -> {
                        log.error("Vehicle not found with id {}", dto.getVehicleId());
                        return new RuntimeException("Vehicle not found with id " + dto.getVehicleId());
                    });
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

        TourOptimizer tourOptimizer;

        switch (tour.getTourType().toString()) {
            case "NearestNeighborOptimizer": // Assuming the enum value name
                tourOptimizer = nearestNeighborOptimizer;
                break;
            case "ClarkeWrightOptimizer": // Assuming the enum value name
                tourOptimizer = clarkeWrightOptimizer;
                break;
            default:
                log.error("Unknown tour type {} for Tour ID {}. Cannot optimize.", tour.getTourType(), id);
                throw new RuntimeException("Unknown tour type: " + tour.getTourType());
        }

        log.info("Selected optimizer: {}. Starting calculation.", tourOptimizer.getClass().getSimpleName());

        List<Delivery> optimizedDeliveries = tourOptimizer.calculateOptimalTour(
                tour.getVehicle(),
                tour.getWarehouse(),
                tour.getDeliveries()
        );

        double totalDistance = getTotallDistance(tour.getWarehouse(), optimizedDeliveries);
        log.info("Optimization complete for Tour ID {}. Total distance: {} km.", id, totalDistance);


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
        log.info("Starting comparison between NEAREST_NEIGHBOR and CLARKE_WRIGHT for Tour ID {}.", id);

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


        log.info("Executing NEAREST_NEIGHBOR optimization.");
        List<Delivery> nnDeliveries = nearestNeighborOptimizer.calculateOptimalTour(
                tour.getVehicle(), tour.getWarehouse(), tour.getDeliveries()
        );
        double nnDistance = getTotallDistance(tour.getWarehouse(), nnDeliveries);
        log.info("NEAREST_NEIGHBOR distance for Tour ID {}: {} km.", id, nnDistance);


        OptimizedTourDTO nnResult = optimizedTourMapper.toDTO(tour, nnDeliveries, nnDistance);
        nnResult.setAlgorithmUsed("NEAREST_NEIGHBOR");
        comparisonResults.add(nnResult);



        log.info("Executing CLARKE_WRIGHT optimization.");
        List<Delivery> cwDeliveries = clarkeWrightOptimizer.calculateOptimalTour(
                tour.getVehicle(), tour.getWarehouse(), tour.getDeliveries()
        );
        double cwDistance = getTotallDistance(tour.getWarehouse(), cwDeliveries);
        log.info("CLARKE_WRIGHT distance for Tour ID {}: {} km.", id, cwDistance);


        OptimizedTourDTO cwResult = optimizedTourMapper.toDTO(tour, cwDeliveries, cwDistance);
        cwResult.setAlgorithmUsed("CLARKE_WRIGHT");
        comparisonResults.add(cwResult);

        log.info("Comparison complete for Tour ID {}. NN: {} km, CW: {} km.", id, nnDistance, cwDistance);

        return comparisonResults;
    }
}