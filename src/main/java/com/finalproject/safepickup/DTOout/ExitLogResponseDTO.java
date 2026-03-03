package com.finalproject.safepickup.DTOout;

import com.finalproject.safepickup.Model.ExitLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExitLogResponseDTO {

    private String studentName;
    private String nfcUid;
    private LocalDateTime requestTime;
    private Boolean status;

    // Constructor from ExitLog entity
    public ExitLogResponseDTO(ExitLog exitLog) {
        this.studentName = exitLog.getStudent() != null ?
                exitLog.getStudent().getName() : "N/A";

        this.nfcUid = exitLog.getNfcCard() != null ?
                exitLog.getNfcCard().getUid() : "N/A";

        this.requestTime = exitLog.getRequestTime();
        this.status = exitLog.isIsAccepted();
    }
}