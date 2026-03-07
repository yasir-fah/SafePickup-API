package com.finalproject.safepickup.Repository;


import com.finalproject.safepickup.Model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Parent findParentById(Integer id);

    @Query("select p from Parent p where p.isAccepted = true")
    List<Parent> findParentByAccepted();

}
