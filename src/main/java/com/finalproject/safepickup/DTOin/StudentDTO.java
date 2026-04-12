package com.finalproject.safepickup.DTOin;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {


    @Size(min = 3, max = 50, message = "username should be between 3 and 50")
    private String name;

    @NotEmpty(message = "grade can't be empty")
    private String Grade;

    @NotEmpty(message = "NationalId can't be empty")
    @Size(min = 10, max = 10, message = "NationalId must be exactly 10 characters")
    private String NationalId;

    @NotNull(message = "School latitude is required")
    private double SchoolLat;

    @NotNull(message = "School longitude is required")
    private double SchoolLon;

}
