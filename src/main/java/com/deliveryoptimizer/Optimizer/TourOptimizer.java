package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface TourOptimizer {

    List<Delivery> calculateOptimalTour(RouteOptimizationContext context);
}
