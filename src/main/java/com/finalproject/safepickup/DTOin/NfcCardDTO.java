package com.finalproject.safepickup.DTOin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NfcCardDTO {

    @NotEmpty(message = "UID cannot be empty")
    @Size(min = 8, max = 20, message = "UID should be between 8 and 20 characters")
    private String uid;

    @NotEmpty(message = "Status cannot be empty")
    @Pattern(
            regexp = "^(FREE|RESERVED)$",
            message = "Status must be either 'FREE' or 'RESERVED'"
    )
    private String status;


}