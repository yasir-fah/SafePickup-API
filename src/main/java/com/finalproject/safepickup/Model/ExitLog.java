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

    // add student id (many-to-one)

    // add parent id (man@y-to-one)

    // add NfcCard id (many-to-one)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime ScanTime;

    @Column(updatable = false)
    private LocalDateTime RequestTime; // when parent request exit

    @Column(updatable = false)
    private LocalDateTime ExpiresAt; // calculated manually (add schedular)

    private boolean IsAccepted = false;

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
