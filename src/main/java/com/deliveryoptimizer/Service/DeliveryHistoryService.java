package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.Model.DeliveryHistory;
import com.deliveryoptimizer.Model.TourStatus;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import com.deliveryoptimizer.Repositories.DeliveryHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service

public class DeliveryHistoryService implements DeliveryHistoryServiceInterface{

    private final DeliveryHistoryRepository deliveryHistoryRepository;
    public DeliveryHistoryService(DeliveryHistoryRepository deliveryHistoryRepository) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
    }

    public Page<DeliveryHistory>findByTourStatus(TourStatus tourStatus, Pageable pageable) {

        return deliveryHistoryRepository.findByTourStatus(tourStatus, pageable);
    }

    public Page<DeliveryHistory> findByCustomerName(@Param("name") String name, Pageable pageable) {
        return  deliveryHistoryRepository.findByCustomerName(name,pageable);
    }

}
