package com.workshop.vehicle.vehicle_service.domain.exceptions;

import com.workshop.vehicle.vehicle_service.infrastructure.configuration.JacocoAnnotationGenerated;

@JacocoAnnotationGenerated
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
