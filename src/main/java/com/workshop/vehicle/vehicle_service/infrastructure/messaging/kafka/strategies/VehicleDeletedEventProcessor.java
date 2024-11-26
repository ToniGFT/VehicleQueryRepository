package com.workshop.vehicle.vehicle_service.infrastructure.messaging.kafka.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("VEHICLE_DELETED")
public class VehicleDeletedEventProcessor implements EventProcessor<VehicleDeletedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDeletedEventProcessor.class);

    private final VehicleEventProcessor vehicleEventProcessor;
    private final ObjectMapper objectMapper;

    public VehicleDeletedEventProcessor(VehicleEventProcessor vehicleEventProcessor, ObjectMapper objectMapper) {
        this.vehicleEventProcessor = vehicleEventProcessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> process(String message) {
        logger.info("Starting to process VEHICLE_DELETED event. Message: {}", message);
        try {
            logger.debug("Deserializing message into VehicleDeletedEvent");
            VehicleDeletedEvent event = objectMapper.readValue(message, VehicleDeletedEvent.class);
            logger.info("Successfully deserialized VehicleDeletedEvent. Event: {}", event);

            return vehicleEventProcessor.processVehicleDeletedEvent(event)
                    .doOnSubscribe(subscription -> logger.info("Subscribed to VehicleDeletedEvent processing. Event: {}", event))
                    .doOnSuccess(result -> logger.info("Successfully processed VehicleDeletedEvent. Event: {}", event))
                    .doOnError(error -> logger.error("Error processing VehicleDeletedEvent. Event: {}, Error: {}", event, error.getMessage()))
                    .doFinally(signalType -> logger.info("Finished processing VEHICLE_DELETED event with signal: {}", signalType));
        } catch (Exception e) {
            logger.error("Failed to process VehicleDeletedEvent. Message: {}, Error: {}", message, e.getMessage(), e);
            return Mono.error(new RuntimeException("Failed to process VehicleDeletedEvent", e));
        }
    }
}
