package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import com.deliveryoptimizer.util.DistanceCalculator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Profile("cw")
@Qualifier("tourOptimizer")
@Component
public class ClarkeWrightOptimizer implements TourOptimizer {

    // 🛑 FIX: Signature changed to accept ONLY the context object (OCP Compliance)
    @Override
    public List<Delivery> calculateOptimalTour(RouteOptimizationContext context) {

        // 1. EXTRACT DATA FROM CONTEXT
        Warehouse warehouse = context.getWarehouse();
        List<Delivery> deliveries = context.getDeliveries();
        // Vehicle vehicle = context.getVehicle(); // Extracted but not used in CW distance logic

        if (deliveries == null || deliveries.isEmpty()) {
            return new ArrayList<>();
        }

        List<List<Delivery>> routes = new ArrayList<>();
        for (Delivery d : deliveries) {
            List<Delivery> route = new ArrayList<>();
            route.add(d);
            routes.add(route);
        }

        // Saving class remains internal and correct
        class Saving implements Comparable<Saving> {
            Delivery d1, d2;
            double saving;

            Saving(Delivery d1, Delivery d2, double saving) {
                this.d1 = d1;
                this.d2 = d2;
                this.saving = saving;
            }

            @Override
            public int compareTo(Saving o) {
                return Double.compare(o.saving, this.saving); // descending sort
            }
        }

        List<Saving> savingsList = new ArrayList<>();
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery d1 = deliveries.get(i);
                Delivery d2 = deliveries.get(j);

                // Assuming DistanceCalc uses (LAT, LON, LAT, LON) based on common practice,
                // but maintaining your original order if it was (LON, LAT, LON, LAT)
                double dist_W_d1 = DistanceCalculator.DistanceCalc(
                        warehouse.getLongitude(), warehouse.getLatitude(),
                        d1.getLongitude(), d1.getLatitude()
                );
                double dist_W_d2 = DistanceCalculator.DistanceCalc(
                        warehouse.getLongitude(), warehouse.getLatitude(),
                        d2.getLongitude(), d2.getLatitude()
                );
                double dist_d1_d2 = DistanceCalculator.DistanceCalc(
                        d1.getLongitude(), d1.getLatitude(),
                        d2.getLongitude(), d2.getLatitude()
                );

                double s = dist_W_d1 + dist_W_d2 - dist_d1_d2;
                savingsList.add(new Saving(d1, d2, s));
            }
        }

        Collections.sort(savingsList);

        for (Saving s : savingsList) {
            List<Delivery> route1 = findRoute(routes, s.d1);
            List<Delivery> route2 = findRoute(routes, s.d2);

            if (route1 == route2) continue;

            List<Delivery> mergedRoute = mergeRoutesIfValid(route1, route2, s.d1, s.d2);

            if (mergedRoute != null) {
                routes.remove(route1);
                routes.remove(route2);
                routes.add(mergedRoute);
            }
        }

        List<Delivery> optimizedDeliveries = new ArrayList<>();
        for (List<Delivery> route : routes) {
            optimizedDeliveries.addAll(route);
        }

        return optimizedDeliveries;
    }

    private List<Delivery> findRoute(List<List<Delivery>> routes, Delivery d) {
        for (List<Delivery> route : routes) {
            // Note: Since this is V1 logic, we assume the stream operation is acceptable
            if (route.stream().anyMatch(delivery -> delivery.getId() == d.getId())) {
                return route;
            }
        }
        return null;
    }

    private List<Delivery> mergeRoutesIfValid(List<Delivery> r1, List<Delivery> r2, Delivery d1, Delivery d2) {

        // This is a simplified merge logic that assumes d1 and d2 are endpoints,
        // which is standard for the basic CW algorithm's merge rule.

        int d1Index = r1.indexOf(d1);
        boolean d1IsHead = d1Index == 0;
        boolean d1IsTail = d1Index == r1.size() - 1;

        int d2Index = r2.indexOf(d2);
        boolean d2IsHead = d2Index == 0;
        boolean d2IsTail = d2Index == r2.size() - 1;

        List<Delivery> merged = null;

        // Case 1: Route 1 Tail meets Route 2 Head (r1 -> r2)
        if (d1IsTail && d2IsHead) {
            merged = new ArrayList<>(r1);
            merged.addAll(r2);
        }
        // Case 2: Route 2 Tail meets Route 1 Head (r2 -> r1)
        else if (d2IsTail && d1IsHead) {
            merged = new ArrayList<>(r2);
            merged.addAll(r1);
        }
        // Case 3: Both Heads meet (W <- d1 d2 -> W). Must reverse one and merge.
        else if (d1IsHead && d2IsHead) {
            List<Delivery> r1Rev = new ArrayList<>(r1);
            Collections.reverse(r1Rev); // W <- d1 is now d1 -> W
            merged = new ArrayList<>(r1Rev); // Now d1 is the head of the merged route
            merged.addAll(r2);
        }
        // Case 4: Both Tails meet (W -> d1 d2 <- W). Must reverse one and merge.
        else if (d1IsTail && d2IsTail) {
            List<Delivery> r2Rev = new ArrayList<>(r2);
            Collections.reverse(r2Rev);
            merged = new ArrayList<>(r1);
            merged.addAll(r2Rev);
        }
        // Case 5: Invalid merge (neither d1 nor d2 is an endpoint)
        else {
            return null;
        }

        return merged;
    }
}