package com.deliveryoptimizer.Optimizer;

import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.Warehouse;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@Data
public class RouteOptimizationContext {
    private Long tourId;
    private Vehicle vehicle;
    private Warehouse warehouse;
    private List<Delivery> deliveries;
}
