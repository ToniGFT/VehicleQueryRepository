package com.workshop.vehicle.vehicle_service.domain.mapper;

import com.workshop.vehicle.vehicle_service.domain.events.VehicleCreatedEvent;
import com.workshop.vehicle.vehicle_service.domain.events.VehicleUpdatedEvent;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import org.modelmapper.ModelMapper;

public class VehicleEventMapper {

    private static final ModelMapper modelMapper = com.workshop.vehicle.vehicle_service.domain.model.mapper.ModelMapperConfig.getModelMapper();

    private VehicleEventMapper() {
    }

    public static Vehicle toVehicle(VehicleCreatedEvent event) {
        return modelMapper.map(event, Vehicle.class);
    }

    public static Vehicle toVehicle(VehicleUpdatedEvent event, Vehicle existingVehicle) {
        modelMapper.map(event, existingVehicle);
        return existingVehicle;
    }
}
