package com.workshop.vehicle.vehicle_service.application.service;

import com.workshop.vehicle.vehicle_service.domain.exceptions.VehicleServiceException;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import com.workshop.vehicle.vehicle_service.domain.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class VehicleQueryServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleQueryServiceImpl vehicleQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVehicleById_ShouldReturnVehicle_WhenVehicleExists() {
        // Arrange
        ObjectId vehicleId = new ObjectId();
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Mono.just(vehicle));

        // Act & Assert
        StepVerifier.create(vehicleQueryService.getVehicleById(vehicleId))
                .expectNext(vehicle)
                .verifyComplete();
    }

    @Test
    void getVehicleById_ShouldThrowVehicleServiceException_WhenErrorOccurs() {
        // Arrange
        ObjectId vehicleId = new ObjectId();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(vehicleQueryService.getVehicleById(vehicleId))
                .expectError(VehicleServiceException.class)
                .verify();
    }

    @Test
    void getAllVehicles_ShouldReturnAllVehicles() {
        // Arrange
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        when(vehicleRepository.findAll()).thenReturn(Flux.just(vehicle1, vehicle2));

        // Act & Assert
        StepVerifier.create(vehicleQueryService.getAllVehicles())
                .expectNext(vehicle1)
                .expectNext(vehicle2)
                .verifyComplete();
    }
}
