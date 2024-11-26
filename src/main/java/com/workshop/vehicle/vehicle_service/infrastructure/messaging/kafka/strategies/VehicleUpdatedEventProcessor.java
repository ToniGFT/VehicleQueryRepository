package com.workshop.vehicle.vehicle_service.infrastructure.messaging.kafka.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.vehicle.vehicle_service.application.service.kafka.VehicleEventProcessor;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("VEHICLE_UPDATED")
public class VehicleUpdatedEventProcessor implements EventProcessor<VehicleUpdatedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(VehicleUpdatedEventProcessor.class);

    private final VehicleEventProcessor vehicleEventProcessor;
    private final ObjectMapper objectMapper;

    public VehicleUpdatedEventProcessor(VehicleEventProcessor vehicleEventProcessor, ObjectMapper objectMapper) {
        this.vehicleEventProcessor = vehicleEventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> process(String message) {
        logger.info("Starting to process VEHICLE_UPDATED event. Message: {}", message);
        try {
            logger.debug("Deserializing message into VehicleUpdatedEvent");
            VehicleUpdatedEvent event = objectMapper.readValue(message, VehicleUpdatedEvent.class);
            logger.info("Successfully deserialized VehicleUpdatedEvent. Event: {}", event);

            return vehicleEventProcessor.processVehicleUpdatedEvent(event)
                    .doOnSubscribe(subscription -> logger.info("Subscribed to VehicleUpdatedEvent processing. Event: {}", event))
                    .doOnSuccess(result -> logger.info("Successfully processed VehicleUpdatedEvent. Event: {}", event))
                    .doOnError(error -> logger.error("Error processing VehicleUpdatedEvent. Event: {}, Error: {}", event, error.getMessage()))
                    .doFinally(signalType -> logger.info("Finished processing VEHICLE_UPDATED event with signal: {}", signalType));
        } catch (Exception e) {
            logger.error("Failed to process VehicleUpdatedEvent. Message: {}, Error: {}", message, e.getMessage(), e);
            return Mono.error(new RuntimeException("Failed to process VehicleUpdatedEvent", e));
        }
    }
}
