package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Mapper.DeliveryMapper;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.DeliveryStatus;
import com.deliveryoptimizer.Model.Tour;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import com.deliveryoptimizer.Repositories.DeliveryRepository;
import com.deliveryoptimizer.Repositories.TourRepository;
import com.deliveryoptimizer.util.VehicleCapacityChecker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Slf4j
public class DeliveryService implements DeleveryServiceInrerface {

    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;
    private final CustomerRepository customerRepository;
    private final DeliveryMapper deliveryMapper;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           TourRepository tourRepository,
                           CustomerRepository customerRepository,
                           DeliveryMapper deliveryMapper) {
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
        this.customerRepository = customerRepository;
        this.deliveryMapper = deliveryMapper;
    }

    @Transactional
    @Override
    public DeliveryDTO create(DeliveryDTO dto) {

        log.info("Starting creation process for new delivery.");

        Delivery newDelivery = deliveryMapper.toEntity(dto);

        // Set Customer if provided
        if (dto.getCustomerId() != null) {
            log.debug("Fetching Customer ID {} for delivery.", dto.getCustomerId());
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id " + dto.getCustomerId()));
            newDelivery.setCustomer(customer);
        }

        // Set Tour if provided
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

        // Map back to DTO and manually set the IDs
        DeliveryDTO responseDTO = deliveryMapper.toDTO(savedDelivery);
        responseDTO.setCustomerId(dto.getCustomerId());
        responseDTO.setTourId(dto.getTourId());

        return responseDTO;
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

    public DeliveryDTO deliverDelivery(Integer id) {
        Delivery delivery =deliveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Delivery ID not found with id " + id));
       delivery.setStatus(DeliveryStatus.DELIVERED);
       delivery.setActualTime(LocalTime.now());
       deliveryRepository.save(delivery);
       return deliveryMapper.toDTO(delivery);
}}