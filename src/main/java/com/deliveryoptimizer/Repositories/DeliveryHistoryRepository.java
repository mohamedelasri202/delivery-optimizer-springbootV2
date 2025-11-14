package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.DTO.AiTrainingDTO;
import com.deliveryoptimizer.Model.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate; // Use modern LocalDate
import java.util.List;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {

    @Query("SELECT new com.deliveryoptimizer.DTO.AiTrainingDTO(" +
            "h.delay_seconds, " +
            "h.dayOfWeek, " +
            "c.preferredTimeSlot, " +
            "c.latitude, " +
            "c.longitude, " +
            "w.latitude, " +
            "w.longitude, " +
            "v.maxWeight, " +
            "v.type, " +
            "d.weight, " +
            "d.volume) " +
            "FROM DeliveryHistory h " +
            "JOIN h.customer c " +
            "JOIN h.delivery d " +
            "JOIN h.tour t " +
            "JOIN t.vehicle v " +
            "JOIN t.warehouse w " +
            "WHERE h.date_of_delivery >= :startDate")
    List<AiTrainingDTO> findAiTrainingData(@Param("startDate") LocalDate startDate);
}