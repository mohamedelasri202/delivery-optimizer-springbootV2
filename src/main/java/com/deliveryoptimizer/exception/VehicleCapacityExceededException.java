package com.deliveryoptimizer.exception;


public class VehicleCapacityExceededException extends RuntimeException {


    public VehicleCapacityExceededException(String message) {
        super(message);
    }


    public VehicleCapacityExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}