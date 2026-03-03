package com.finalproject.safepickup.DTOout;

import com.finalproject.safepickup.Model.NfcCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NfcCardResponseDTO {

    private Integer id;
    private String uId;
    private String status;


    public NfcCardResponseDTO(NfcCard nfcCard) {
        this.id = nfcCard.getId();
        this.uId = nfcCard.getUid();
        this.status = nfcCard.getStatus();
    }

}