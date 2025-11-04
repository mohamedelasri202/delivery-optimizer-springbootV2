package com.deliveryoptimizer.DTO;

import java.util.List;

public class OptimizedTourDTO {

    private Integer tourId;
    private VehicleInfo vehicle;
    private WarehouseInfo warehouse;
    private List<DeliveryInfo> deliveries;
    private double totalDistance;

    private String algorithmUsed;

    public OptimizedTourDTO() {}


    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public void setAlgorithmUsed(String algorithmUsed) {
        this.algorithmUsed = algorithmUsed;
    }
    // Getters and Setters
    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public VehicleInfo getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleInfo vehicle) {
        this.vehicle = vehicle;
    }

    public WarehouseInfo getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseInfo warehouse) {
        this.warehouse = warehouse;
    }

    public List<DeliveryInfo> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryInfo> deliveries) {
        this.deliveries = deliveries;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }


    public static class VehicleInfo {
        private Integer id;
        private String type;
        private Double maxWeight;
        private Double maxVolume;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Double getMaxWeight() {
            return maxWeight;
        }

        public void setMaxWeight(Double maxWeight) {
            this.maxWeight = maxWeight;
        }

        public Double getMaxVolume() {
            return maxVolume;
        }

        public void setMaxVolume(Double maxVolume) {
            this.maxVolume = maxVolume;
        }
    }

    public static class WarehouseInfo {
        private Integer id;
        private String name;
        private Double latitude;
        private Double longitude;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public static class DeliveryInfo {
        private Integer id;

        private Double latitude;
        private Double longitude;
        private Double weight;
        private Double volume;
        private String status;

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }





        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}