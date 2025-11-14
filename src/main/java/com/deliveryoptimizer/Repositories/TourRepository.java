package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.Model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {





    @Query("""
SELECT DISTINCT t
FROM Tour t
JOIN FETCH t.warehouse
JOIN FETCH t.vehicle
LEFT JOIN FETCH t.deliveries
WHERE t.id = :id
""")
    Optional<Tour> findByIdWithDetails(@Param("id") Integer id);

}