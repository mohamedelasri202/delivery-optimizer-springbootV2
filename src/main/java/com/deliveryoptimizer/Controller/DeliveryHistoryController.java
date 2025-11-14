package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.Model.DeliveryHistory;
import com.deliveryoptimizer.Model.TourStatus;
import com.deliveryoptimizer.Service.DeliveryHistoryServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class DeliveryHistoryController {

    private final DeliveryHistoryServiceInterface deliveryHistoryServiceInterface;

    public DeliveryHistoryController(DeliveryHistoryServiceInterface deliveryHistoryServiceInterface) {
        this.deliveryHistoryServiceInterface = deliveryHistoryServiceInterface;
    }

    @GetMapping("/by-tour-status")
    public Page<DeliveryHistory> getDeliveryHistory(
            @RequestParam TourStatus status,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam (defaultValue = "dayOfWeek") String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return deliveryHistoryServiceInterface.findByTourStatus(status, pageable);
    }

    @GetMapping("/customer_name")
    public  Page<DeliveryHistory> getDeliveryHistoryByCustomerName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dayOfWeek") String sortBy
    ){
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortBy));
       return   deliveryHistoryServiceInterface.findByCustomerName(name,pageable);
    }
}