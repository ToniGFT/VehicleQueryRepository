package com.workshop.vehicle.vehicle_service.domain.model.mapper;

import com.workshop.vehicle.vehicle_service.domain.events.VehicleUpdatedEvent;
import com.workshop.vehicle.vehicle_service.domain.model.aggregates.Vehicle;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class ModelMapperConfig {

    @Getter
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<VehicleUpdatedEvent, Vehicle>() {
            @Override
            protected void configure() {
                skip(destination.getVehicleId());
            }
        });
    }

    private ModelMapperConfig() {
    }
}
