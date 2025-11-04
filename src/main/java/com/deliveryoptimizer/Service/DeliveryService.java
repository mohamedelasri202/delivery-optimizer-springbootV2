package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Mapper.DeliveryMapper;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Repositories.DeliveryRepository;
import com.deliveryoptimizer.Repositories.TourRepository;
import com.deliveryoptimizer.util.VehicleCapacityChecker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeliveryService implements DeleveryServiceInrerface {

    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryService(DeliveryRepository deliveryRepository, TourRepository tourRepository, DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
        this.deliveryMapper = deliveryMapper;
    }

    @Transactional
    @Override
    public Delivery create(DeliveryDTO dto) {

        log.info("Starting creation process for new delivery.");

        Delivery newDelivery = deliveryMapper.toEntity(dto);

        if (dto.getTourId() != null) {


            log.debug("Attempting to fetch Tour ID {} for validation.", dto.getTourId());


            Tour tour = tourRepository.findByIdWithDetails(dto.getTourId().intValue())
                    .orElseThrow(() -> new RuntimeException("Tour not found with id " + dto.getTourId()));


            log.debug("Tour ID {} fetched successfully. Starting capacity check.", tour.getId());


            VehicleCapacityChecker.validateNewDeliveryLoad(
                    tour.getId(),
                    tour.getVehicle(),
                    tour.getDeliveries(),
                    newDelivery
            );


            log.info("Capacity check passed. Linking delivery to Tour ID {}.", tour.getId());

            newDelivery.setTour(tour);
        }

        Delivery savedDelivery = deliveryRepository.save(newDelivery);

        log.info("Delivery ID {} successfully created and saved.", savedDelivery.getId());

        return savedDelivery;
    }

    @Transactional
    @Override
    public Delivery update(Delivery delivery) {
        log.info("Updating delivery ID {}.", delivery.getId());
        Delivery updatedDelivery = deliveryRepository.save(delivery);
        log.info("Delivery ID {} successfully updated.", updatedDelivery.getId());
        return updatedDelivery;
    }

    @Transactional
    @Override
    public void delete(int id) {
        log.warn("Attempting to delete delivery ID {}.", id);
        deliveryRepository.deleteById(id);
        log.info("Delivery ID {} successfully deleted.", id);
    }
}