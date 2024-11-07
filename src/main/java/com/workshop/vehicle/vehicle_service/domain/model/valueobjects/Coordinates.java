package com.workshop.vehicle.vehicle_service.domain.model.valueobjects;

import com.workshop.vehicle.vehicle_service.infrastructure.configuration.JacocoAnnotationGenerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JacocoAnnotationGenerated
public class Coordinates {

    @NotNull(message = "Latitude is required")
    @Pattern(regexp = "^([+\\-])?(?:90\\.0{1,6}?|(?:[0-9]|[1-8][0-9])(?:\\.[0-9]{1,6})?)$",
            message = "Latitude must be between -90 and 90")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Pattern(regexp = "^([+\\-])?(?:180\\.0{1,6}?|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:\\.[0-9]{1,6})?)$",
            message = "Longitude must be between -180 and 180")
    private Double longitude;
}
