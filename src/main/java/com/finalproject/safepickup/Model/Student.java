package com.finalproject.safepickup.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    // connect parent id (many-to-one)
    // code here
    @Column(nullable = false, name = "username")
    @Size(min = 3, max = 50, message = "username should be between 3 and 50")
    private String Name;

    @Column(nullable = false)
    private String Grade;


    private double SchoolLat;

    private double SchoolLon;

    private double Radius;

    /* Relationships:   */

    @ManyToOne
    @JsonIgnore
    private Parent parent;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<NfcCard> nfcCards;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<ExitLog> exitLogs;
}
