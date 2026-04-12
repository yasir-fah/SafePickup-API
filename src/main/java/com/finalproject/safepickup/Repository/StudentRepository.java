package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findStudentById(Integer id);
    List<Student> findAllByParentId(Integer parentId);
}
