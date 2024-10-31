package com.workshop.vehicle.vehicle_service.application.response.service;

import com.workshop.vehicle.vehicle_service.application.response.builder.VehicleResponseBuilder;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleResponseServiceTest {

    @InjectMocks
    private VehicleResponseService vehicleResponseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buildOkResponse_ShouldReturnOkResponseWithVehicle() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        ResponseEntity<Vehicle> okResponse = VehicleResponseBuilder.generateOkResponse(vehicle);

        // Act & Assert
        StepVerifier.create(vehicleResponseService.buildOkResponse(vehicle))
                .assertNext(response -> assertEquals(okResponse, response))
                .verifyComplete();
    }

    @Test
    void buildVehiclesResponse_ShouldReturnOkResponsesForEachVehicle() {
        // Arrange
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        ResponseEntity<Vehicle> okResponse1 = VehicleResponseBuilder.generateOkResponse(vehicle1);
        ResponseEntity<Vehicle> okResponse2 = VehicleResponseBuilder.generateOkResponse(vehicle2);

        // Act & Assert
        StepVerifier.create(vehicleResponseService.buildVehiclesResponse(Flux.just(vehicle1, vehicle2)))
                .expectNext(okResponse1)
                .expectNext(okResponse2)
                .verifyComplete();
    }
}
