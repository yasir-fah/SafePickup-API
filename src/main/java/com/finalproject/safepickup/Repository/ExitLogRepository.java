package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.ExitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExitLogRepository extends JpaRepository<ExitLog,Integer> {
    List<ExitLog> findByStudentId(Integer studentId);

    List<ExitLog> findByParentId(Integer parentId);
}
