package com.finalproject.safepickup.Repository;


import com.finalproject.safepickup.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Parent findParentById(Integer id);
    boolean existsByNationalId(String nationalId);
    boolean existsByNationalIdAndIdNot(String nationalId, Integer id);
}
