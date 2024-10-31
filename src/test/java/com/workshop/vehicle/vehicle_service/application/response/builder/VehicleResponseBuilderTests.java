package com.workshop.vehicle.vehicle_service.application.response.builder;

import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VehicleResponseBuilderTest {

    @Test
    void generateOkResponse_ShouldReturnOkResponseWithVehicle() {
        // Arrange
        Vehicle vehicle = new Vehicle();

        // Act
        ResponseEntity<Vehicle> response = VehicleResponseBuilder.generateOkResponse(vehicle);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vehicle, response.getBody());
    }
}
