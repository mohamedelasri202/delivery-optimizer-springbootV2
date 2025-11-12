package com.deliveryoptimizer.DTO;

import java.time.LocalTime;
import java.util.List;

// Note: No need for @Entity or other JPA annotations here
public class CustomerDTO {
    private Long id;
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private String preferredTimeSlot;

    // Represents the list of Deliveries by their IDs
    private List<Long> deliveryIds;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPreferredTimeSlot() {
        return preferredTimeSlot;
    }

    public void setPreferredTimeSlot(String preferredTimeSlot) {
        this.preferredTimeSlot = preferredTimeSlot;
    }

    // --- Getters and Setters (Abbreviated for clarity, assume all are present) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Long> getDeliveryIds() { return deliveryIds; }
    public void setDeliveryIds(List<Long> deliveryIds) { this.deliveryIds = deliveryIds; }
}