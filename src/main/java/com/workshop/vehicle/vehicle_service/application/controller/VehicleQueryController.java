package com.workshop.vehicle.vehicle_service.application.controller;

import com.workshop.vehicle.vehicle_service.application.response.service.VehicleResponseService;
import com.workshop.vehicle.vehicle_service.application.service.VehicleQueryService;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/vehicles")
public class VehicleQueryController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleQueryController.class);
    private final VehicleQueryService vehicleQueryService;
    private final VehicleResponseService vehicleResponseService;

    public VehicleQueryController(VehicleQueryService vehicleQueryService, VehicleResponseService vehicleResponseService) {
        this.vehicleQueryService = vehicleQueryService;
        this.vehicleResponseService = vehicleResponseService;
    }

    @GetMapping("/{idString}")
    public Mono<ResponseEntity<Vehicle>> getVehicleById(@PathVariable("idString") String idString) {
        ObjectId id = new ObjectId(idString);
        logger.info("Fetching vehicle with ID: {}", id);
        return vehicleQueryService.getVehicleById(id)
                .flatMap(vehicleResponseService::buildOkResponse)
                .doOnSuccess(response -> logger.info("Successfully fetched vehicle with ID: {}", id))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Vehicle> getAllVehicles() {
        logger.info("Fetching all vehicles");
        return vehicleQueryService.getAllVehicles()
                .doFirst(() -> logger.info("Starting to fetch all vehicles"))
                .doOnComplete(() -> logger.info("Successfully fetched all vehicles"));
    }

}
