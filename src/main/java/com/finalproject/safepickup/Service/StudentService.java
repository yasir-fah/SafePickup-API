package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.StudentDTO;
import com.finalproject.safepickup.Model.Parent;
import com.finalproject.safepickup.Model.Student;
import com.finalproject.safepickup.Repository.ParentRepository;
import com.finalproject.safepickup.Repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    // 1- get all student
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public void addStudent(StudentDTO dto) {
        // 1- check if parent with this ID exist
        Parent parent = parentRepository.findParentByNationalId(dto.getParentNationalId());

        if (parent == null) {
            throw new ApiException("Parent not found");
        }

        // 2- check if parent accepted
        if(!parent.isAccepted()){
            throw new ApiException("Parent not accepted Yet");
        }

        // 3- create new student
        Student student = new Student();
        student.setName(dto.getName());
        student.setGrade(dto.getGrade());
        student.setSchoolLat(dto.getSchoolLat());
        student.setSchoolLon(dto.getSchoolLon());

        // 4- link parent
        student.setParent(parent);

        // 5- save
        studentRepository.save(student);
    }

    // 3- Update student
    public void updateStudent(Integer studentId, StudentDTO dto) {
        // 1- Find existing student
        Student oldStudent = studentRepository.findStudentById(studentId);
        if(oldStudent == null) {
            throw new ApiException("Student not found");
        }

        // 2- Find parent by national ID
        Parent parent = parentRepository.findParentByNationalId(dto.getParentNationalId());

        if(parent == null) {
            throw new ApiException("Parent with National ID " + dto.getParentNationalId() + " not found");
        }

        // 3- Check if parent is accepted
        if(!parent.isAccepted()) {
            throw new ApiException("Parent account is not yet accepted by admin");
        }

        // 4- Update student fields
        oldStudent.setName(dto.getName());
        oldStudent.setGrade(dto.getGrade());
        oldStudent.setSchoolLat(dto.getSchoolLat());
        oldStudent.setSchoolLon(dto.getSchoolLon());

        // 5- Update parent link (in case it changed)
        oldStudent.setParent(parent);

        // 6- Save
        studentRepository.save(oldStudent);
    }

    // 4- Delete student
    public void deleteStudent(Integer studentId) {
        // 1- Find existing student
        Student student = studentRepository.findStudentById(studentId);
        if(student == null) {
            throw new ApiException("Student not found");
        }

        // 2- Delete
        studentRepository.delete(student);
    }
}
