package com.workshop.vehicle.vehicle_service.infrastructure.messaging.kafka.strategies;

import reactor.core.publisher.Mono;

public interface EventProcessor<T> {
    Mono<Void> process(String message);
}
