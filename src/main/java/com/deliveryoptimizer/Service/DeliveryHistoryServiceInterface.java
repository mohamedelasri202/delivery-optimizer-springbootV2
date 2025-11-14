package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.Model.DeliveryHistory;
import com.deliveryoptimizer.Model.TourStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface DeliveryHistoryServiceInterface {
    Page<DeliveryHistory> findByTourStatus(TourStatus tourStatus, Pageable pageable);
    Page<DeliveryHistory> findByCustomerName(@Param("name") String name, Pageable pageable);
}
