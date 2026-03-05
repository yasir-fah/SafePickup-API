package com.finalproject.safepickup.DTOout;

import com.finalproject.safepickup.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    private String username;
    private String grade;

    // Constructor from Student entity
    public StudentResponseDTO(Student student) {
        this.username = student.getName();
        this.grade = student.getGrade();
    }
}