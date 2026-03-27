package com.finalproject.safepickup.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = true)
    private LocalDateTime ScanTime;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime RequestTime; // when parent request exit

    @Column(updatable = true)
    private LocalDateTime ExpiresAt;

    private LocalDateTime lastOtpSentAt;

    private boolean IsAccepted = false;

    private boolean IsWithinRadius = false;

    private boolean IsOtpVerified = false;

    // private Boolean isBiometricVerified = false;  // TODO: Add later for biometric


    private String parentLat; // ask from UI

    private String parentLon; // ask from UI


    /* Relationships:   */

    @ManyToOne
    @JsonIgnore
    private Parent parent;

    @ManyToOne
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JsonIgnore
    private NfcCard NfcCard;
}
