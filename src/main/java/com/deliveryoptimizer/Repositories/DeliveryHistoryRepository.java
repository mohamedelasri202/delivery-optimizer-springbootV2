package com.deliveryoptimizer.Repositories;

import com.deliveryoptimizer.Model.DeliveryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {
}
