package com.finalproject.safepickup.DTOout;

import com.finalproject.safepickup.Model.Parent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentResponseDTO {

    private Integer id;
    private String username;
    private String nationalId;
    private String phone;
    private String status;  // "approved" or "pending"

    // Constructor from Parent entity
    public ParentResponseDTO(Parent parent) {
        this.id = parent.getId();
        this.username = parent.getUser() != null ? parent.getUser().getUsername() : null;
        this.nationalId = parent.getNationalId();
        this.phone = parent.getPhone();
        this.status = parent.isAccepted() ? "approved" : "pending";
    }
}