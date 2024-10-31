package com.workshop.vehicle.vehicle_service.application.service;

import com.workshop.vehicle.vehicle_service.domain.exceptions.VehicleServiceException;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import com.workshop.vehicle.vehicle_service.domain.repository.VehicleRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Mono<Vehicle> getVehicleById(ObjectId vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .onErrorMap(e -> new VehicleServiceException("Error querying vehicles by route ID"));
    }

    @Override
    public Flux<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
