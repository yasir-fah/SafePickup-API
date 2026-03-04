package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.StudentDTO;
import com.finalproject.safepickup.Service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.status(200).body(studentService.findAll());
    }

    @PostMapping("/add/student")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDTO studentDTO) {
        studentService.addStudent(studentDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Student added successfully"));
    }

    @PutMapping("/update/student/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer studentId,
                                           @RequestBody @Valid StudentDTO dto) {
        studentService.updateStudent(studentId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Student updated successfully"));
    }

    @DeleteMapping("/delete/student/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.status(200).body(new ApiResponse("Student deleted successfully"));
    }
}
