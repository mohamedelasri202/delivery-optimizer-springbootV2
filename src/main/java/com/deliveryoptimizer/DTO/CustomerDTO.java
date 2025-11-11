package com.deliveryoptimizer.DTO;


import java.util.List;

public class CustomerDTO {
    private Long id;
    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private String preferredTimeSlot;
    private List<Long> deliveryIds;


}