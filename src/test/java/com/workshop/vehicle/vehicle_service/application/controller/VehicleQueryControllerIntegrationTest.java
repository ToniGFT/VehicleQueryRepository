package com.workshop.vehicle.vehicle_service.application.controller;

import com.workshop.vehicle.vehicle_service.application.response.service.VehicleResponseService;
import com.workshop.vehicle.vehicle_service.application.service.VehicleQueryService;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import com.workshop.vehicle.vehicle_service.domain.model.entities.Driver;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.Contact;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.Coordinates;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.MaintenanceDetails;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.enums.VehicleStatus;
import com.workshop.vehicle.vehicle_service.domain.model.valueobjects.enums.VehicleType;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(controllers = VehicleQueryController.class)
public class VehicleQueryControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VehicleQueryService vehicleQueryService;

    @MockBean
    private VehicleResponseService vehicleResponseService;

    private Vehicle sampleVehicle;

    @BeforeEach
    public void setUp() {
        sampleVehicle = Vehicle.builder()
                .vehicleId(new ObjectId("672b1343bb9d3b2fdd48ac14"))
                .licensePlate("XYZ123")
                .capacity(50)
                .currentStatus(VehicleStatus.IN_SERVICE)
                .type(VehicleType.BUS)
                .driver(Driver.builder()
                        .name("John Doe")
                        .contact(Contact.builder()
                                .email("john.doe@example.com")
                                .phone("+123456789")
                                .build())
                        .build())
                .maintenanceDetails(MaintenanceDetails.builder()
                        .scheduledBy("Admin")
                        .scheduledDate(LocalDate.now().plusDays(10))
                        .details("Routine check")
                        .build())
                .currentLocation(new Coordinates(40.7128, -74.0060))
                .routeId(new ObjectId("672b1343bb9d3b2fdd48ac15"))
                .build();

        when(vehicleQueryService.getVehicleById(any(ObjectId.class))).thenReturn(Mono.just(sampleVehicle));
        when(vehicleQueryService.getAllVehicles()).thenReturn(Flux.just(sampleVehicle));

        when(vehicleResponseService.buildOkResponse(any(Vehicle.class)))
                .thenAnswer(invocation -> Mono.just(ResponseEntity.ok(invocation.getArgument(0))));
    }

    @Test
    public void testGetVehicleById() {
        webTestClient.get()
                .uri("/vehicles/672b1343bb9d3b2fdd48ac14")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vehicle.class)
                .value(vehicle -> {
                    assertThat(vehicle).isNotNull();
                    assertThat(vehicle.getLicensePlate()).isEqualTo("XYZ123");
                    assertThat(vehicle.getCapacity()).isEqualTo(50);
                    assertThat(vehicle.getCurrentStatus()).isEqualTo(VehicleStatus.IN_SERVICE);
                    assertThat(vehicle.getType()).isEqualTo(VehicleType.BUS);
                });
    }

    @Test
    public void testGetAllVehicles() {
        webTestClient.get()
                .uri("/vehicles")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Vehicle.class)
                .value(vehicles -> {
                    assertThat(vehicles).isNotEmpty();
                    assertThat(vehicles.get(0).getLicensePlate()).isEqualTo("XYZ123");
                    assertThat(vehicles.get(0).getDriver().getName()).isEqualTo("John Doe");
                });
    }
}
