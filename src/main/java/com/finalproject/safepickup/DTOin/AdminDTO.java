package com.finalproject.safepickup.DTOin;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    // From User (all except role)
    @Size(min = 3, max = 50, message = "username should be between 3 and 50")
    private String username;

    @Size(min = 7, message = "Password must be at least 7 characters long")
    private String password;

    private String email;

    // admin does not need (phone & nationalId)
}
