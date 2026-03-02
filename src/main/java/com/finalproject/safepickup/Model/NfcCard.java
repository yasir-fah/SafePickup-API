package com.finalproject.safepickup.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @Column(unique = true)
    private String UId;

    // add student (one-to-one)
    @Pattern(
            regexp = "^(FREE|RESERVED)$",
            message = "Status must be either 'FREE' or 'RESERVED'"
    )
    private String status;


    @Column(updatable = false)
    private LocalDateTime IssuedAt;
}
