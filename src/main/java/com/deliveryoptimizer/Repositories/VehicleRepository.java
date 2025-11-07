package com.deliveryoptimizer.Repositories;



import com.deliveryoptimizer.Model.Vehicle;
import com.deliveryoptimizer.Model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

}

