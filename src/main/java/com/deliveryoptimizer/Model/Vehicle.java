package com.deliveryoptimizer.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    private Double maxWeight; // Capacity in kg
    private Double maxVolume; // Capacity in m³
    private Integer maxDeliveries; // Max deliveries per tour

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tour> tours;

    public Vehicle() {}


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public Double getMaxWeight() { return maxWeight; }
    public void setMaxWeight(Double maxWeight) { this.maxWeight = maxWeight; }

    public Double getMaxVolume() { return maxVolume; }
    public void setMaxVolume(Double maxVolume) { this.maxVolume = maxVolume; }

    public Integer getMaxDeliveries() { return maxDeliveries; }
    public void setMaxDeliveries(Integer maxDeliveries) { this.maxDeliveries = maxDeliveries; }


    public List<Tour> getTours() { return tours; }
    public void setTours(List<Tour> tours) { this.tours = tours; }
}
