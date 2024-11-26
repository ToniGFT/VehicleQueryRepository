package com.workshop.vehicle.vehicle_service.application.service.kafka;

import com.workshop.vehicle.vehicle_service.domain.events.VehicleCreatedEvent;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleDeletedEvent;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleUpdatedEvent;
import reactor.core.publisher.Mono;

public interface VehicleEventProcessor {
    Mono<Void> processVehicleCreatedEvent(VehicleCreatedEvent event);

    Mono<Void> processVehicleDeletedEvent(VehicleDeletedEvent event);

    Mono<Void> processVehicleUpdatedEvent(VehicleUpdatedEvent event);
}
