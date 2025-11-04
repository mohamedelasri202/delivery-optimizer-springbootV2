package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.Model.Warehouse;
import java.util.List;
import java.util.Optional;

public interface WareHouseServiceInterface {

    Warehouse createWarehouse(Warehouse warehouse);

    Warehouse updateWarehouse(int id, Warehouse warehouse);

    Boolean deleteWarehouse(int id);


}
