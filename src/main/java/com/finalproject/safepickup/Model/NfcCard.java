package com.finalproject.safepickup.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NfcCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotEmpty(message = "UID can't be empty")
    @Column(unique = true, nullable = false)
    private String UId;

    // add student (one-to-one)
    @Pattern(
            regexp = "^(FREE|RESERVED)$",
            message = "Status must be either 'FREE' or 'RESERVED'"
    )
    private String status;


    @Column(updatable = false)
    private LocalDateTime IssuedAt; // issued when (connected with student)

    /* Relationships:   */

    @ManyToOne
    @JsonIgnore
    private Student student;

    @OneToMany(mappedBy = "NfcCard",  cascade = CascadeType.ALL) // mappedBy: point to the name of Entity at the other side
    private Set<ExitLog> exitLogs;
}
