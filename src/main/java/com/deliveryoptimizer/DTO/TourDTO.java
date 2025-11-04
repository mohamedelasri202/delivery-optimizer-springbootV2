package com.deliveryoptimizer.DTO;

import java.time.LocalDate;

public class TourDTO {

    private int id;
    private LocalDate date;
    private String tourType;

    private Integer warehouseId;
    private String warehouseName;

    private Long vehicleId;
    private String vehicleRegistrationNumber;
    private String vehicleType;

    public TourDTO() {}



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTourType() {
        return tourType;
    }
    public void setTourType(String tourType) {
        this.tourType = tourType;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Long getVehicleId() {
        return vehicleId;
    }
    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
