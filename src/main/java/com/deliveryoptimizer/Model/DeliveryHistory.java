package com.deliveryoptimizer.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
@Getter @Setter
@NoArgsConstructor
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate date_of_delivery;
    private LocalTime planned_time;
    private LocalTime actual_time;
    private Long delay_seconds;
    @ManyToOne
    @JoinColumn(name ="delivery_id")
    private Delivery delivery;
    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
