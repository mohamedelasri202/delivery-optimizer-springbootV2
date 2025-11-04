package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Mapper.DeliveryMapper;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Repositories.DeliveryRepository;
import com.deliveryoptimizer.Repositories.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {


    @Mock
    private DeliveryRepository deliveryRepository;


    @Mock
    private DeliveryMapper deliveryMapper;


    @Mock
    private TourRepository tourRepository;


    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery mockDelivery;
    private DeliveryDTO mockDeliveryDTO;

    private final Long TEST_DELIVERY_ID = 1L;

    @BeforeEach
    void setUp() {

        mockDelivery = new Delivery();
        mockDelivery.setId(TEST_DELIVERY_ID);
        mockDelivery.setWeight(50.0);


        mockDeliveryDTO = new DeliveryDTO();
        mockDeliveryDTO.setWeight(50.0);
    }

    @Test
    void create_ShouldSaveDeliveryWithoutTourId() {

        when(deliveryMapper.toEntity(any(DeliveryDTO.class))).thenReturn(mockDelivery);
        when(deliveryRepository.save(mockDelivery)).thenReturn(mockDelivery);


        Delivery created = deliveryService.create(mockDeliveryDTO);


        assertThat(created).isEqualTo(mockDelivery);


        verify(deliveryMapper, times(1)).toEntity(mockDeliveryDTO);
        verify(deliveryRepository, times(1)).save(mockDelivery);


        verify(tourRepository, never()).findByIdWithDetails(anyInt());
    }


    @Test
    void update_ShouldSaveTheUpdatedDelivery() {

        mockDelivery.setWeight(150.0);

        when(deliveryRepository.save(mockDelivery)).thenReturn(mockDelivery);

        Delivery updated = deliveryService.update(mockDelivery);


        assertThat(updated.getId()).isEqualTo(TEST_DELIVERY_ID);
        assertThat(updated.getWeight()).isEqualTo(150.0);

        verify(deliveryRepository, times(1)).save(mockDelivery);
    }
}