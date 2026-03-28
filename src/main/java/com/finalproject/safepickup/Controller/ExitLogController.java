package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Service.ExitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/exitlog")
@RequiredArgsConstructor
public class ExitLogController {

    private final ExitLogService exitLogService;

    // ==================== ADMIN endpoints ====================

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllExitLogs() {
        return ResponseEntity.status(200).body(exitLogService.findAll());
    }

    @GetMapping("/get/student/{studentId}")
    public ResponseEntity<?> getExitLogsByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.status(200).body(exitLogService.findByStudentId(studentId));
    }

    @GetMapping("/admin/logs")
    public ResponseEntity<?> getAllLogsForAdmin() {
        return ResponseEntity.status(200).body(exitLogService.findAllLogsForAdmin());
    }

    // ==================== PARENT endpoints (self-operations) ====================

    @GetMapping("/get/my-logs")
    public ResponseEntity<?> getMyExitLogs(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(exitLogService.findByParentId(user.getId()));
    }

    @GetMapping("/parent/logs")
    public ResponseEntity<?> getStudentLogsForParent(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(exitLogService.studentLogForParent(user.getId()));
    }
}
