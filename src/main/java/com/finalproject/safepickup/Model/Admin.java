package com.finalproject.safepickup.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Admin {

    private Integer id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime LastLoginAT;
}
