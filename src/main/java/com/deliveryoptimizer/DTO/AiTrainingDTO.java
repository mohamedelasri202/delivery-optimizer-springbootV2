package com.deliveryoptimizer.DTO;

import com.deliveryoptimizer.Model.DayOfWeek;
import com.deliveryoptimizer.Model.VehicleType;
import lombok.AllArgsConstructor; // 🎯 FIX 1: Generates the required constructor
import lombok.Data; // Provides getters, setters, equals, hashCode
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiTrainingDTO {


    private Long delay_seconds;


    private DayOfWeek dayOfWeek;
    private String customerPreferredTimeSlot;


    private double customerLatitude;
    private double customerLongitude;
    private double warehouseLatitude;
    private double warehouseLongitude;


    private double vehicleMaxWeight;
    private VehicleType vehicleType;
    private double packageWeight;
    private double packageVolume;
    public AiTrainingDTO(Long delay_second,
                         DayOfWeek dayOfWeek,
                         String customerPreferredTimeSlot,
                         Double customerLatitude,
                         Double customerLongitude,
                         Double warehouseLatitude,
                         Double warehouseLongitude,
                         Double vehicleMaxWeight,
                         VehicleType vehicleType,
                         Double packageWeight,
                         Double packageVolume) {
        this.delay_seconds = delay_second;
        this.dayOfWeek = dayOfWeek;
        this.customerPreferredTimeSlot = customerPreferredTimeSlot;
        this.customerLatitude = customerLatitude;
        this.customerLongitude = customerLongitude;
        this.warehouseLatitude = warehouseLatitude;
        this.warehouseLongitude = warehouseLongitude;
        this.vehicleMaxWeight = vehicleMaxWeight;
        this.vehicleType = vehicleType;
        this.packageWeight = packageWeight;
        this.packageVolume = packageVolume;

    }

}