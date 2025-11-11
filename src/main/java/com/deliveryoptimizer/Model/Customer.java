package com.deliveryoptimizer.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private String preferredTimeSlot;
    @OneToMany(mappedBy = "customer" ,cascade = CascadeType.ALL)
    private List<Delivery> deliveries;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<DeliveryHistory> historyRecords;


}
