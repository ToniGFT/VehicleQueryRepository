package com.workshop.vehicle.vehicle_service.application.controller;

import com.workshop.vehicle.vehicle_service.application.response.service.VehicleResponseService;
import com.workshop.vehicle.vehicle_service.application.service.VehicleQueryService;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class VehicleQueryControllerTest {

    @Mock
    private VehicleQueryService vehicleQueryService;

    @Mock
    private VehicleResponseService vehicleResponseService;

    @InjectMocks
    private VehicleQueryController vehicleQueryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVehicleById_ShouldReturnVehicle_WhenVehicleExists() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        ObjectId vehicleId = new ObjectId(id);
        Vehicle vehicle = new Vehicle();
        when(vehicleQueryService.getVehicleById(vehicleId)).thenReturn(Mono.just(vehicle));
        when(vehicleResponseService.buildOkResponse(vehicle)).thenReturn(Mono.just(ResponseEntity.ok(vehicle)));

        // Act & Assert
        StepVerifier.create(vehicleQueryController.getVehicleById(id))
                .expectNext(ResponseEntity.ok(vehicle))
                .verifyComplete();
    }

    @Test
    void getVehicleById_ShouldReturnNotFound_WhenVehicleDoesNotExist() {
        // Arrange
        String id = "507f1f77bcf86cd799439011";
        ObjectId vehicleId = new ObjectId(id);
        when(vehicleQueryService.getVehicleById(vehicleId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(vehicleQueryController.getVehicleById(id))
                .expectNext(ResponseEntity.notFound().build())
                .verifyComplete();
    }


    @Test
    void getAllVehicles_ShouldReturnAllVehicles() {
        // Arrange
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        when(vehicleQueryService.getAllVehicles()).thenReturn(Flux.just(vehicle1, vehicle2));

        // Act & Assert
        StepVerifier.create(vehicleQueryController.getAllVehicles())
                .expectNext(vehicle1)
                .expectNext(vehicle2)
                .verifyComplete();
    }
}
