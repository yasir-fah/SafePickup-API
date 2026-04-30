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


    @Size(min = 3, max = 50, message = "name should be between 3 and 50")
    @NotEmpty(message = "name is required")
    private String name;

    @NotEmpty(message = "grade is required")
    private String Grade;

    @NotEmpty(message = "NationalId is required")
    @Size(min = 10, max = 10, message = "NationalId is required, with exactly 10 digits")
    @Pattern(regexp = "^\\d{10}$", message = "NationalId is required, with exactly 10 digits")
    @Column(unique = true)
    private String NationalId;

    @NotNull(message = "School latitude is required")
    private double SchoolLat;

    @NotNull(message = "School longitude is required")
    private double SchoolLon;

}
