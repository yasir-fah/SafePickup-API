package com.finalproject.safepickup.DTOin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExitRequestDTO {

    @NotNull(message = "Parent latitude is required")
    private Double parentLat;

    @NotNull(message = "Parent longitude is required")
    private Double parentLon;
}