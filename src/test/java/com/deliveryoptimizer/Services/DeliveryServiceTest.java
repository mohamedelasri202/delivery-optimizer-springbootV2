package com.deliveryoptimizer.Service;

import com.deliveryoptimizer.DTO.DeliveryDTO;
import com.deliveryoptimizer.Mapper.DeliveryMapper;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Model.Delivery;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import com.deliveryoptimizer.Repositories.DeliveryRepository;
import com.deliveryoptimizer.Repositories.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private Delivery mockDelivery;
    private DeliveryDTO mockDeliveryDTO;
    private DeliveryDTO mockResponseDTO;
    private Customer mockCustomer;

    private final Long TEST_DELIVERY_ID = 1L;
    private final Long TEST_CUSTOMER_ID = 10L;

    @BeforeEach
    void setUp() {
        // Setup mock delivery entity
        mockDelivery = new Delivery();
        mockDelivery.setId(TEST_DELIVERY_ID);
        mockDelivery.setWeight(50.0);

        // Setup mock customer
        mockCustomer = new Customer();
        mockCustomer.setId(TEST_CUSTOMER_ID);
        mockCustomer.setName("Test Customer");

        // Setup mock input DTO
        mockDeliveryDTO = new DeliveryDTO();
        mockDeliveryDTO.setWeight(50.0);
        mockDeliveryDTO.setCustomerId(TEST_CUSTOMER_ID);

        // Setup mock response DTO
        mockResponseDTO = new DeliveryDTO();
        mockResponseDTO.setId(TEST_DELIVERY_ID);
        mockResponseDTO.setWeight(50.0);
        mockResponseDTO.setCustomerId(TEST_CUSTOMER_ID);
    }

    @Test
    void create_ShouldSaveDeliveryWithoutTourId() {
        // Arrange
        when(deliveryMapper.toEntity(any(DeliveryDTO.class))).thenReturn(mockDelivery);
        when(customerRepository.findById(TEST_CUSTOMER_ID)).thenReturn(Optional.of(mockCustomer));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(mockDelivery);
        when(deliveryMapper.toDTO(mockDelivery)).thenReturn(mockResponseDTO);

        // Act
        DeliveryDTO created = deliveryService.create(mockDeliveryDTO);

        // Assert
        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(TEST_DELIVERY_ID);
        assertThat(created.getWeight()).isEqualTo(50.0);
        assertThat(created.getCustomerId()).isEqualTo(TEST_CUSTOMER_ID);

        // Verify interactions
        verify(deliveryMapper, times(1)).toEntity(mockDeliveryDTO);
        verify(customerRepository, times(1)).findById(TEST_CUSTOMER_ID);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
        verify(deliveryMapper, times(1)).toDTO(mockDelivery);

        // Verify tour repository was never called
        verify(tourRepository, never()).findByIdWithDetails(anyInt());
    }

    @Test
    void create_ShouldSaveDeliveryWithoutCustomerId() {
        // Arrange - DTO without customer ID
        DeliveryDTO dtoWithoutCustomer = new DeliveryDTO();
        dtoWithoutCustomer.setWeight(50.0);

        DeliveryDTO responseDTOWithoutCustomer = new DeliveryDTO();
        responseDTOWithoutCustomer.setId(TEST_DELIVERY_ID);
        responseDTOWithoutCustomer.setWeight(50.0);

        when(deliveryMapper.toEntity(any(DeliveryDTO.class))).thenReturn(mockDelivery);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(mockDelivery);
        when(deliveryMapper.toDTO(mockDelivery)).thenReturn(responseDTOWithoutCustomer);

        // Act
        DeliveryDTO created = deliveryService.create(dtoWithoutCustomer);

        // Assert
        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(TEST_DELIVERY_ID);

        // Verify customer repository was never called
        verify(customerRepository, never()).findById(any());
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void update_ShouldSaveTheUpdatedDelivery() {
        // Arrange
        mockDelivery.setWeight(150.0);
        when(deliveryRepository.save(mockDelivery)).thenReturn(mockDelivery);

        // Act
        Delivery updated = deliveryService.update(mockDelivery);

        // Assert
        assertThat(updated.getId()).isEqualTo(TEST_DELIVERY_ID);
        assertThat(updated.getWeight()).isEqualTo(150.0);

        // Verify
        verify(deliveryRepository, times(1)).save(mockDelivery);
    }
}