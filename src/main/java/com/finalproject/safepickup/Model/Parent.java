package com.finalproject.safepickup.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Entity
public class Parent {

    private Integer id;

    @NotEmpty(message = "National ID can't be empty")
    @Size(min = 10, max = 10, message = "ID must be exactly length of 10 ")
    @Pattern(regexp = "^\\d{10}$", message = "ID must contain only 10 digits")
    @Column(unique = true, nullable = false)
    private String NationalId;

    @Pattern(regexp = "^\\+9665\\d{8}$", message = "Phone number must be a valid Saudi mobile in the format +9665XXXXXXXX")
    @Column(unique = true, nullable = false)
    private String Phone;

    private boolean isAccepted = false;
}
