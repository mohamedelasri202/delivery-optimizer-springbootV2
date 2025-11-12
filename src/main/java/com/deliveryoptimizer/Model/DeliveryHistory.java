package com.deliveryoptimizer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope; // Required for Prototype
import org.springframework.stereotype.Component; // Required for Spring management

import java.time.LocalDate;
import java.time.LocalTime;
// Removed unused java.util.List import

@Entity
@Component
@Scope("prototype") // 🛑 CRITICAL: Ensures a NEW instance is created for every lookup call
@Getter @Setter
@NoArgsConstructor
public class DeliveryHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    private LocalDate date_of_delivery;


    @Column(name = "plannedTime")
    private LocalTime planned_time;

    @Column(name = "actualTime")
    private LocalTime actual_time;

    private Long delay_seconds;


    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;



    @ManyToOne
    @JoinColumn(name ="delivery_id")
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}