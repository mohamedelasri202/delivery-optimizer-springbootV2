package com.deliveryoptimizer.DTO;

import com.deliveryoptimizer.Model.VehicleType;
import java.util.List;

public class VehicleDTO {

    private Long id;
    private VehicleType type;
    private Double maxWeight;
    private Double maxVolume;
    private Integer maxDeliveries;



    private List<Integer> tourIds;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getType() {
        return type;
    }
    public void setType(VehicleType type) {
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

    public Integer getMaxDeliveries() {
        return maxDeliveries;
    }
    public void setMaxDeliveries(Integer maxDeliveries) {
        this.maxDeliveries = maxDeliveries;
    }


    public List<Integer> getTourIds() {
        return tourIds;
    }
    public void setTourIds(List<Integer> tourIds) {
        this.tourIds = tourIds;
    }
}
