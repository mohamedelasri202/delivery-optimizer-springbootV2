package com.deliveryoptimizer.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // FIX: To suppress nested JSON output
import jakarta.persistence.*;
import java.time.LocalTime; // Modern time standard
import java.util.List;

@Entity
// FIX: Instruct Jackson to ignore complex navigation properties in API response
@JsonIgnoreProperties({"customer", "tour", "historyRecords"})
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Recommended: Changed to Long for consistency

    private double latitude;
    private double longitude;

    private double weight;
    private double volume;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PENDING;


    @Column(name = "plannedTime")
    private LocalTime plannedTime;

    @Column(name = "actualTime")
    private LocalTime actualTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;


    @OneToMany(mappedBy = "delivery", cascade = CascadeType.REMOVE)
    private List<DeliveryHistory> historyRecords;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "tour_id")
    private Tour tour;

 public Delivery() {}




    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }


    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }


    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }


    public LocalTime getPlannedTime() { return plannedTime; }
    public void setPlannedTime(LocalTime plannedTime) { this.plannedTime = plannedTime; }

    public LocalTime getActualTime() { return actualTime; }
    public void setActualTime(LocalTime actualTime) { this.actualTime = actualTime; }


    public Tour getTour() { return tour; }
    public void setTour(Tour tour) { this.tour = tour; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<DeliveryHistory> getHistoryRecords() { return historyRecords; }
    public void setHistoryRecords(List<DeliveryHistory> historyRecords) { this.historyRecords = historyRecords; }
}