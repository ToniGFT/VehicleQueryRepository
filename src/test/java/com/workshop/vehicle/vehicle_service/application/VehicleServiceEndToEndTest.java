package com.workshop.vehicle.vehicle_service.application;

import com.workshop.vehicle.vehicle_service.VehicleServiceApplication;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.enums.VehicleStatus;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.enums.VehicleType;
import com.workshop.vehicle.vehicle_service.domain.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = VehicleServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class VehicleServiceEndToEndTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setupDatabase() {
        vehicleRepository.deleteAll().block();

        Vehicle vehicle1 = Vehicle.builder()
                .vehicleId(new ObjectId())
                .licensePlate("ABC123")
                .capacity(50)
                .currentStatus(VehicleStatus.IN_SERVICE)
                .type(VehicleType.BUS)
                .build();

        Vehicle vehicle2 = Vehicle.builder()
                .vehicleId(new ObjectId())
                .licensePlate("XYZ789")
                .capacity(60)
                .currentStatus(VehicleStatus.OUT_OF_SERVICE)
                .type(VehicleType.TRAM)
                .build();

        vehicleRepository.saveAll(List.of(vehicle1, vehicle2)).collectList().block();
    }

    @Test
    void getVehicleById_EndToEnd_ShouldReturnExpectedVehicle() {
        Vehicle vehicle = vehicleRepository.findAll().blockFirst();

        webTestClient.get()
                .uri("/vehicles/{idString}", vehicle.getVehicleId().toHexString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vehicle.class)
                .consumeWith(response -> {
                    Vehicle vehicleResponse = response.getResponseBody();
                    assert vehicleResponse != null;
                    assertEquals(vehicleResponse.getLicensePlate(), "ABC123");
                    assertEquals(vehicleResponse.getCapacity(), 50);
                    assertEquals(vehicleResponse.getCurrentStatus(), VehicleStatus.IN_SERVICE);
                });
    }

    @Test
    void getAllVehicles_EndToEnd_ShouldReturnAllVehicles() {
        webTestClient.get()
                .uri("/vehicles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vehicle.class)
                .consumeWith(response -> {
                    List<Vehicle> responseBody = response.getResponseBody();
                    assert responseBody != null;
                    assertEquals(responseBody.size(), 2);

                    Vehicle vehicle1 = responseBody.stream()
                            .filter(v -> "ABC123".equals(v.getLicensePlate()))
                            .findFirst()
                            .orElseThrow(() -> new AssertionError("Vehicle ABC123 not found"));

                    Vehicle vehicle2 = responseBody.stream()
                            .filter(v -> "XYZ789".equals(v.getLicensePlate()))
                            .findFirst()
                            .orElseThrow(() -> new AssertionError("Vehicle XYZ789 not found"));

                    assertEquals(vehicle1.getLicensePlate(), "ABC123");
                    assertEquals(vehicle2.getLicensePlate(), "XYZ789");
                });
    }

    @Test
    void getVehicleById_NotFound_ShouldReturn404() {
        ObjectId nonExistentId = new ObjectId();

        webTestClient.get()
                .uri("/vehicles/{idString}", nonExistentId.toHexString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
