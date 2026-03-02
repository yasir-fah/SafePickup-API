package com.finalproject.safepickup.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Admin {

    @Id
    private Integer id;

    @CreationTimestamp
    @Column(updatable = true)
    private LocalDateTime LastLoginAt;

    /* Relationship:   */
    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;
}
