package com.finalproject.safepickup.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Parent {

    @Id
    private Integer id;


    @Column(unique = true, nullable = false)
    private String NationalId;

    @Column(unique = true, nullable = false)
    private String Phone;

    private boolean isAccepted = false;

    /* Relationships:   */
    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Student> students;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<ExitLog> exitLogs;
}
