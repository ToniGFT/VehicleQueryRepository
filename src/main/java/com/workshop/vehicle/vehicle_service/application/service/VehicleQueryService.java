package com.workshop.vehicle.vehicle_service.application.service;

import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleQueryService {

    Mono<Vehicle> getVehicleById(ObjectId routeId);

    Flux<Vehicle> getAllVehicles();
}
