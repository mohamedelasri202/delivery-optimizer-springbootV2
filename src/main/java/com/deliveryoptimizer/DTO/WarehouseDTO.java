package com.deliveryoptimizer.DTO;

import java.util.List;

public class WarehouseDTO {

    private long id;
    private String name;
    private double latitude;
    private double longitude;


    private List<Integer> tourIds;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public List<Integer> getTourIds() { return tourIds; }
    public void setTourIds(List<Integer> tourIds) { this.tourIds = tourIds; }
}
