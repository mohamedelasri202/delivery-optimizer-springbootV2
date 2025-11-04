package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

//import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

import static com.deliveryoptimizer.Model.TourType.ClarkeWrightOptimizer;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Rollback(false)
public class TourRepositoryTest {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private WareHouseRepository wareHouseRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private Tour savedTour;

    @BeforeEach
    void setup() {
        // 🏭 Create warehouse
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Test Warehouse");
        warehouse.setLatitude(33.5731);
        warehouse.setLongitude(-7.5898);
        wareHouseRepository.saveAndFlush(warehouse);

        //  Create vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setType(VehicleType.TRUCK);
        vehicle.setMaxWeight(2000.0);
        vehicle.setMaxVolume(25.0);
        vehicle.setMaxDeliveries(50);
        vehicleRepository.saveAndFlush(vehicle);

        //  Create tour
        Tour tour = new Tour();
        tour.setTourType(ClarkeWrightOptimizer);
        tour.setWarehouse(warehouse);
        tour.setVehicle(vehicle);

        Delivery d1 = new Delivery();
        d1.setLatitude(33.6);
        d1.setLongitude(-7.5);
        d1.setTour(tour);

        Delivery d2 = new Delivery();
        d2.setLatitude(33.61);
        d2.setLongitude(-7.52);
        d2.setTour(tour);

        tour.setDeliveries(Arrays.asList(d1, d2));

        savedTour = tourRepository.saveAndFlush(tour);
    }
    @Test
    void testFindByIdWithDetails_ShouldReturnTourWithAllDetails() {
        Optional<Tour> result = tourRepository.findByIdWithDetails(savedTour.getId());

        assertThat(result).isPresent();
        Tour tour = result.get();

        // Warehouse details
        Warehouse wh = tour.getWarehouse();
        System.out.println("Warehouse ID: " + wh.getId());
        System.out.println("Warehouse Name: " + wh.getName());
        System.out.println("Warehouse Latitude: " + wh.getLatitude());
        System.out.println("Warehouse Longitude: " + wh.getLongitude());

        // Vehicle details
        Vehicle v = tour.getVehicle();
        System.out.println("Vehicle ID: " + v.getId());
        System.out.println("Vehicle Type: " + v.getType());
        System.out.println("Max Weight: " + v.getMaxWeight());
        System.out.println("Max Volume: " + v.getMaxVolume());
        System.out.println("Max Deliveries: " + v.getMaxDeliveries());

        // Delivery details
        for (Delivery d : tour.getDeliveries()) {
            System.out.println("Delivery ID: " + d.getId());
            System.out.println("Latitude: " + d.getLatitude());
            System.out.println("Longitude: " + d.getLongitude());
            System.out.println("Weight: " + d.getWeight());
            System.out.println("Volume: " + d.getVolume());
            System.out.println("Status: " + d.getStatus());
        }
    }



}
