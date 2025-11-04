package com.deliveryoptimizer.util;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.VehicleType; // Assuming you have this enum
import com.deliveryoptimizer.exception.VehicleCapacityExceededException; // Assuming you have this custom exception
import lombok.extern.slf4j.Slf4j; // Required for @Slf4j
import java.util.List;


@Slf4j
public class VehicleCapacityChecker {

    private static int getMaxStopsForType(VehicleType vehicleType) {
        if (vehicleType == null) return 0;

        switch (vehicleType) {
            case BIKE: return 15;
            case VAN: return 50;
            case TRUCK: return 100;
            default: return 0;
        }
    }

    public static void validateNewDeliveryLoad(Integer tourId, Vehicle vehicle, List<Delivery> existingDeliveries, Delivery newDelivery) {

        log.info("Starting capacity validation for Tour ID: {}. Vehicle Type: {}. Max Weight: {}kg, Max Volume: {}m³.",
                tourId, vehicle.getType(), vehicle.getMaxWeight(), vehicle.getMaxVolume());

        // 1. Calculate the total potential load
        double totalWeight = existingDeliveries.stream().mapToDouble(Delivery::getWeight).sum() + newDelivery.getWeight();
        double totalVolume = existingDeliveries.stream().mapToDouble(Delivery::getVolume).sum() + newDelivery.getVolume();


        // --- Weight Check ---
        if (totalWeight > vehicle.getMaxWeight()) {
            String message = "Weight limit exceeded: " + totalWeight + "kg > " + vehicle.getMaxWeight() + "kg.";
            log.error("Capacity VIOLATION (Weight) for Tour {}: {}", tourId, message);
            throw new VehicleCapacityExceededException(message);
        }

        // --- Volume Check ---
        if (totalVolume > vehicle.getMaxVolume()) {
            String message = "Volume limit exceeded: " + totalVolume + "m³ > " + vehicle.getMaxVolume() + "m³.";
            log.error("Capacity VIOLATION (Volume) for Tour {}: {}", tourId, message);
            throw new VehicleCapacityExceededException(message);
        }


        log.info("Capacity check PASSED for Tour ID {}. New delivery is feasible. Total load: {}kg, {}m³.",
                tourId, totalWeight, totalVolume);
    }
}