package com.finalproject.safepickup.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
