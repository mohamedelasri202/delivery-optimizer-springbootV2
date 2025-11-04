package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.util.DistanceCalculator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class NearestNeighborOptimizer implements TourOptimizer {

    @Override
    public List<Delivery> calculateOptimalTour(Vehicle vehicle, Warehouse warehouse, List<Delivery> deliveries) {

        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        List<Delivery> orderedDeliveries = new ArrayList<>();
        List<Delivery> remaining = new ArrayList<>(deliveries);

        double currentLat = warehouse.getLatitude();
        double currentLon = warehouse.getLongitude();

        while (!remaining.isEmpty()) {
            Delivery nearest = findNearestDeliveryWithReturnCost(
                    currentLat,
                    currentLon,
                    warehouse,
                    remaining
            );
            orderedDeliveries.add(nearest);

            currentLat = nearest.getLatitude();
            currentLon = nearest.getLongitude();

            remaining.remove(nearest);
        }

        return orderedDeliveries;
    }


    private Delivery findNearestDeliveryWithReturnCost(
            double fromLat,
            double fromLon,
            Warehouse warehouse,
            List<Delivery> deliveries
    ) {
        Delivery nearest = null;
        double minTotalCost = Double.MAX_VALUE;

        for (Delivery d : deliveries) {

            double distToDelivery = DistanceCalculator.DistanceCalc(
                    fromLat, fromLon,
                    d.getLatitude(), d.getLongitude()
            );


            double distBackToWarehouse = DistanceCalculator.DistanceCalc(
                    d.getLatitude(), d.getLongitude(),
                    warehouse.getLatitude(), warehouse.getLongitude()
            );


            double totalCost = distToDelivery + distBackToWarehouse;

            if (totalCost < minTotalCost) {
                minTotalCost = totalCost;
                nearest = d;
            }
        }

        return nearest;
    }
}