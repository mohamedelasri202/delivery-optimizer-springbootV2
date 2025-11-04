package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Model.Delivery;

public interface DeleveryServiceInrerface {
    Delivery create(DeliveryDTO dto);
    Delivery update(Delivery delivery);
    void delete(int id);
}
