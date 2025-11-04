package com.deliveryoptimizer.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double latitude;
    private double longitude;

    private double weight;
    private double volume;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "tour_id")
    private Tour tour;

    public Delivery() {}


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    public Tour getTour() { return tour; }
    public void setTour(Tour tour) { this.tour = tour; }
}
