package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Model.Delivery;

import java.time.LocalTime;

public interface DeleveryServiceInrerface {
    DeliveryDTO create(DeliveryDTO dto);  // Changed return type
    Delivery update(Delivery delivery);
    void delete(int id);
    public DeliveryDTO  deliverDelivery(Integer id);
}
