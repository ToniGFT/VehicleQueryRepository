package com.workshop.vehicle.vehicle_service.infrastructure.repository;

import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleCommandRepository extends ReactiveMongoRepository<Vehicle, ObjectId> {
}
