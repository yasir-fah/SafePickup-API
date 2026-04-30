package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findStudentById(Integer id);
    List<Student> findAllByParentId(Integer parentId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.NationalId = :nationalId")
    boolean existsByNationalId(@Param("nationalId") String nationalId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.NationalId = :nationalId AND s.Id != :id")
    boolean existsByNationalIdAndIdNot(@Param("nationalId") String nationalId, @Param("id") Integer id);
}
