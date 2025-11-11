package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("tourOptimizer")
@Profile("nn")
public class NearestNeighborOptimizer implements TourOptimizer {

    // 🛑 FIX: The method signature must accept ONLY the context object.
    @Override
    public List<Delivery> calculateOptimalTour(RouteOptimizationContext context) {

        // 1. EXTRACT DATA FROM CONTEXT (Necessary step after OCP change)
        Warehouse warehouse = context.getWarehouse();
        List<Delivery> deliveries = context.getDeliveries();
        // Vehicle vehicle = context.getVehicle(); // Extracted but not used in NN distance logic

        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        List<Delivery> orderedDeliveries = new ArrayList<>();
        List<Delivery> remaining = new ArrayList<>(deliveries);

        // Warehouse coordinates are the starting coordinates
        double currentLat = warehouse.getLatitude();
        double currentLon = warehouse.getLongitude();

        while (!remaining.isEmpty()) {
            Delivery nearest = findNearestDeliveryWithReturnCost(
                    currentLat,
                    currentLon,
                    warehouse, // Pass warehouse for return distance calc
                    remaining
            );

            // Check if a nearest delivery was actually found
            if (nearest == null) {
                // Should not happen if remaining is not empty, but good defensive coding
                break;
            }

            orderedDeliveries.add(nearest);

            // Update current location
            currentLat = nearest.getLatitude();
            currentLon = nearest.getLongitude();

            remaining.remove(nearest);
        }

        return orderedDeliveries;
    }


    // This helper method does not need to change, as it uses the extracted data.
    private Delivery findNearestDeliveryWithReturnCost(
            double fromLat,
            double fromLon,
            Warehouse warehouse,
            List<Delivery> deliveries
    ) {
        Delivery nearest = null;
        double minTotalCost = Double.MAX_VALUE;

        for (Delivery d : deliveries) {

            // Ensure DistanceCalc parameter order is consistent (LAT, LON, LAT, LON)
            double distToDelivery = DistanceCalculator.DistanceCalc(
                    fromLat, fromLon,
                    d.getLatitude(), d.getLongitude()
            );

            double distBackToWarehouse = DistanceCalculator.DistanceCalc(
                    d.getLatitude(), d.getLongitude(),
                    warehouse.getLatitude(), warehouse.getLongitude()
            );

            // The Nearest Neighbor algorithm logic (always choose the shortest path + return home)
            double totalCost = distToDelivery + distBackToWarehouse;

            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                nearest = d;
            }
        }

        return nearest;
    }
}