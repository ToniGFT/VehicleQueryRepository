package com.workshop.vehicle.vehicle_service.application.service.kafka;

import com.workshop.vehicle.vehicle_service.domain.events.VehicleCreatedEvent;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleDeletedEvent;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleUpdatedEvent;
import com.workshop.vehicle.vehicle_service.domain.mapper.VehicleEventMapper;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import com.workshop.vehicle.vehicle_service.infrastructure.repository.VehicleCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class VehicleEventProcessorImpl implements VehicleEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(VehicleEventProcessorImpl.class);

    private final VehicleCommandRepository vehicleCommandRepository;

    public VehicleEventProcessorImpl(VehicleCommandRepository vehicleCommandRepository) {
        this.vehicleCommandRepository = vehicleCommandRepository;
    }

    @Override
    public Mono<Void> processVehicleCreatedEvent(VehicleCreatedEvent event) {
        logger.info("Processing VehicleCreatedEvent: {}", event);
        Vehicle vehicle = VehicleEventMapper.toVehicle(event);
        logger.debug("Mapped VehicleCreatedEvent to Vehicle: {}", vehicle);

        return vehicleCommandRepository.save(vehicle)
                .doOnSuccess(savedVehicle -> logger.info("Successfully saved vehicle to database: {}", savedVehicle))
                .doOnError(error -> logger.error("Failed to save vehicle to database: {}", error.getMessage(), error))
                .then();
    }

    @Override
    public Mono<Void> processVehicleDeletedEvent(VehicleDeletedEvent event) {
        logger.info("Processing VehicleDeletedEvent: {}", event);
        return vehicleCommandRepository.deleteById(event.getVehicleId())
                .doOnSuccess(unused -> logger.info("Successfully deleted vehicle with ID: {}", event.getVehicleId()))
                .doOnError(error -> logger.error("Failed to delete vehicle with ID: {}", event.getVehicleId(), error))
                .then();
    }

    @Override
    public Mono<Void> processVehicleUpdatedEvent(VehicleUpdatedEvent event) {
        logger.info("Processing VehicleUpdatedEvent: {}", event);
        return vehicleCommandRepository.findById(event.getVehicleId())
                .doOnSubscribe(subscription -> logger.debug("Fetching vehicle with ID: {}", event.getVehicleId()))
                .doOnNext(existingVehicle -> logger.debug("Found existing vehicle: {}", existingVehicle))
                .switchIfEmpty(Mono.defer(() -> {
                    logger.warn("No vehicle found with ID: {}", event.getVehicleId());
                    return Mono.empty();
                }))
                .flatMap(existingVehicle -> {
                    Vehicle updatedVehicle = VehicleEventMapper.toVehicle(event, existingVehicle);
                    logger.debug("Mapped VehicleUpdatedEvent to updated Vehicle: {}", updatedVehicle);
                    return vehicleCommandRepository.save(updatedVehicle)
                            .doOnSuccess(savedVehicle -> logger.info("Successfully updated vehicle in database: {}", savedVehicle));
                })
                .doOnError(error -> logger.error("Failed to update vehicle with ID: {}", event.getVehicleId(), error))
                .then();
    }
}
