package com.finalproject.safepickup.Repository;

import com.finalproject.safepickup.Model.ExitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ExitLogRepository extends JpaRepository<ExitLog,Integer> {
    List<ExitLog> findByStudentId(Integer studentId);

    List<ExitLog> findByParentId(Integer parentId);

    // Find active (not expired, approved, not yet scanned) exit request for a student
    @Query("SELECT e FROM ExitLog e WHERE e.student.Id = :studentId " +
            "AND e.IsAccepted = true AND e.ExpiresAt > :now AND e.ScanTime IS NULL")
    ExitLog findActiveRequestForStudent(@Param("studentId") Integer studentId,
                                        @Param("now") LocalDateTime now);


    @Query("SELECT e FROM ExitLog e WHERE e.parent.id = :parentId " +
            "AND e.IsWithinRadius = true " +
            "AND e.IsOtpVerified = false " +
            "AND e.ExpiresAt > :now " +
            "ORDER BY e.RequestTime DESC")
    List<ExitLog> findPendingExitRequestsByParent(@Param("parentId") Integer parentId,
                                                  @Param("now") LocalDateTime now);
}
