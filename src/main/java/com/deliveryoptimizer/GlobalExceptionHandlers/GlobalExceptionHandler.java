package com.deliveryoptimizer.GlobalExceptionHandlers;

import com.deliveryoptimizer.exception.VehicleCapacityExceededException; // Import your custom exception
import org.springframework.http.HttpStatus; // Required for HttpStatus
import org.springframework.http.ResponseEntity; // Required for ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice; // Required for @ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler; // Required for @ExceptionHandler
import org.springframework.web.context.request.WebRequest; // Required for WebRequest

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(VehicleCapacityExceededException.class)
    public ResponseEntity<Object> handleCapacityExceededException(
            VehicleCapacityExceededException ex, WebRequest request) {


        Map<String, Object> body = new LinkedHashMap<>();


        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Capacity Constraint Violation");


        body.put("message", ex.getMessage());


        String pathDescription = request.getDescription(false);
        if (pathDescription.startsWith("uri=")) {
            body.put("path", pathDescription.substring(4));
        } else {
            body.put("path", pathDescription);
        }



        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}