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
    private String status;

    // Constructor from ExitLog entity
    public ExitLogResponseDTO(ExitLog exitLog) {
        this.studentName = exitLog.getStudent().getName();
        this.nfcUid = exitLog.getNfcCard().getUid();
        this.requestTime = exitLog.getRequestTime();
        this.status = exitLog.isIsAccepted() ? "approved" : "rejected";
    }
}