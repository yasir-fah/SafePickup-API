package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.StudentDTO;
import com.finalproject.safepickup.DTOout.StudentResponseDTO;
import com.finalproject.safepickup.Model.NfcCard;
import com.finalproject.safepickup.Model.Parent;
import com.finalproject.safepickup.Model.Student;
import com.finalproject.safepickup.Repository.NfcCardRepository;
import com.finalproject.safepickup.Repository.ParentRepository;
import com.finalproject.safepickup.Repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final NfcCardRepository nfcCardRepository;

    // 1- get all student
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /* endpoint will be linked: add new student account
     * this for register new student to the system
     */
    public void addStudent(StudentDTO dto) {

        // create new student
        Student student = new Student();
        student.setName(dto.getName());
        student.setGrade(dto.getGrade());
        student.setSchoolLat(dto.getSchoolLat());
        student.setSchoolLon(dto.getSchoolLon());

        studentRepository.save(student);
    }

    /* endpoint will be linked: Link student
     * this will be the <button = Link /> where we link parent with student
     */
    public String linkParentAndStudent(int parent_id, int student_id) {

        Parent parent = parentRepository.findParentById(parent_id);
        if (parent == null) {
            throw new ApiException("Parent not found");
        }

        if (!parent.isAccepted()) {
            throw new ApiException("Parent not accepted");
        }

        Student student = studentRepository.findStudentById(student_id);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        // 4- link parent
        student.setParent(parent);
        studentRepository.save(student);

        return parent.getUser().getUsername();
    }


    // 4- Update student
    public void updateStudent(Integer parentId, Integer studentId, StudentDTO dto) {
        // 1- Find existing student
        Student oldStudent = studentRepository.findStudentById(studentId);
        if (oldStudent == null) {
            throw new ApiException("Student not found");
        }

        // 2- Find parent by national ID
        Parent parent = parentRepository.findParentById(parentId);

        if (parent == null) {
            throw new ApiException("Parent with  ID " + parentId + " not found");
        }

        // 3- Check if parent is accepted
        if (!parent.isAccepted()) {
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

    // 5- Delete student
    public void deleteStudent(Integer studentId) {
        // 1- Find existing student
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        // 2- Delete
        studentRepository.delete(student);
    }


    /* 6- endpoint will be used in UI
     * this will return list of student for link(student & NFCs)
     */
    public List<StudentResponseDTO> findAllStudentForStudentAssignment() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(StudentResponseDTO::new)
                .collect(Collectors.toList());
    }

    /* 7- endpoint will be used in UI
     * this will allow us to connect student with NFC card
     */
    public String linkStudentWithNfc(Integer studentId, Integer nfcId) {
        Student student = studentRepository.findStudentById(studentId);
        if (student == null) {
            throw new ApiException("Student not found");
        }

        NfcCard nfc = nfcCardRepository.findNfcCardById(nfcId);
        if (nfc == null) {
            throw new ApiException("NfcCard not found");
        }

        if (nfc.getStatus().equals("FREE")) {
            nfc.setIssuedAt(LocalDateTime.now());
            nfc.setStatus("RESERVED");
            nfc.setStudent(student);

            nfcCardRepository.save(nfc);
        }
        else {
            throw new ApiException("Nfc Card is Reserved");
        }

        return student.getName();
    }
}
