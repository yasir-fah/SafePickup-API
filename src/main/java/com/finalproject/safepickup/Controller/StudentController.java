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

    @PostMapping("/add/new/student")
    public ResponseEntity<?> addStudent(@Valid @RequestBody StudentDTO studentDTO) {
        studentService.addStudent(studentDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Student added successfully"));
    }

    @PutMapping("/link/parent/{parent_id}/and/student/{student_id}")
    public ResponseEntity<?> linkParentAndStudent(@PathVariable Integer parent_id,@PathVariable Integer student_id) {
        String result = studentService.linkParentAndStudent(parent_id, student_id);
        return  ResponseEntity.status(200).body(new ApiResponse("Student linked Successfully to "+ result));
    }

    @PutMapping("/update/student/{studentId}/parent/{parent_id}")
    public ResponseEntity<?> updateStudent(@PathVariable Integer studentId,
                                           @PathVariable Integer parent_id,
                                           @RequestBody @Valid StudentDTO dto) {
        studentService.updateStudent(parent_id,studentId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Student updated successfully"));
    }

    @DeleteMapping("/delete/student/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Integer studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.status(200).body(new ApiResponse("Student deleted successfully"));
    }
}
