package com.workshop.vehicle.vehicle_service.infrastructure.messaging.kafka.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.vehicle.vehicle_service.application.service.kafka.VehicleEventProcessor;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("VEHICLE_CREATED")
public class VehicleCreatedEventProcessor implements EventProcessor<VehicleCreatedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(VehicleCreatedEventProcessor.class);

    private final VehicleEventProcessor vehicleEventProcessor;
    private final ObjectMapper objectMapper;

    public VehicleCreatedEventProcessor(VehicleEventProcessor vehicleEventProcessor, ObjectMapper objectMapper) {
        this.vehicleEventProcessor = vehicleEventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> process(String message) {
        logger.info("Starting to process VEHICLE_CREATED event. Message: {}", message);
        try {
            logger.debug("Deserializing message into VehicleCreatedEvent");
            VehicleCreatedEvent event = objectMapper.readValue(message, VehicleCreatedEvent.class);
            logger.info("Successfully deserialized VehicleCreatedEvent. Event: {}", event);

            return vehicleEventProcessor.processVehicleCreatedEvent(event)
                    .doOnSubscribe(subscription -> logger.info("Subscribed to VehicleCreatedEvent processing. Event: {}", event))
                    .doOnSuccess(result -> logger.info("Successfully processed VehicleCreatedEvent. Event: {}", event))
                    .doOnError(error -> logger.error("Error processing VehicleCreatedEvent. Event: {}, Error: {}", event, error.getMessage()))
                    .doFinally(signalType -> logger.info("Finished processing VEHICLE_CREATED event with signal: {}", signalType));
        } catch (Exception e) {
            logger.error("Failed to process VehicleCreatedEvent. Message: {}, Error: {}", message, e.getMessage(), e);
            return Mono.error(new RuntimeException("Failed to process VehicleCreatedEvent", e));
        }
    }
}
