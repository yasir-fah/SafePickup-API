package com.finalproject.safepickup.DTOin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentDTO {

    @Size(min = 3, max = 50, message = "username should be between 3 and 50")
    private String username;

    @Size(min = 7, message = "Password must be at least 7 characters long")
    private String password;

    private String email;

    // From Parent (all except isAccepted)
    @NotEmpty(message = "National ID can't be empty")
    @Size(min = 10, max = 10, message = "ID must be exactly length of 10")
    @Pattern(regexp = "^\\d{10}$", message = "ID must contain only 10 digits")
    private String nationalId;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format 05XXXXXXXX")
    private String phone;
}
