package com.finalproject.safepickup.DTOin;

import jakarta.validation.constraints.Email;
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
    @NotEmpty(message = "username is required")
    private String username;

    @Pattern(
            regexp = "^(?=(.*\\d){3})(?=.*[^a-zA-Z0-9]).{7,}$",
            message = "Password must be at least 7 characters long, contain 3 numbers minimum, 1 special character"
    )
    @NotEmpty(message = "password is required")
    private String password;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    // From Parent
    @NotEmpty(message = "National ID is required, and valid National ID Pattern")
    @Size(min = 10, max = 10, message = "NationalId is required, with exactly 10 digits")
    @Pattern(regexp = "^\\d{10}$", message = "NationalId is required, with exactly 10 digits")
    private String nationalId;

    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format 05XXXXXXXX")
    @NotEmpty(message = "phone can't be empty")
    private String phone;
}
