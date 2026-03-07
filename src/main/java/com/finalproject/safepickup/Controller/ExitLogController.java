package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Service.ExitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/exitlog")
@RequiredArgsConstructor
public class ExitLogController {

    private final ExitLogService exitLogService;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllExitLogs() {
        return ResponseEntity.status(200).body(exitLogService.findAll());
    }

    @GetMapping("/get/student/{studentId}")
    public ResponseEntity<?> getExitLogsByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.status(200).body(exitLogService.findByStudentId(studentId));
    }

    @GetMapping("/get/parent/{parentId}")
    public ResponseEntity<?> getExitLogsByParent(@PathVariable Integer parentId) {
        return ResponseEntity.status(200).body(exitLogService.findByParentId(parentId));
    }

    // link to UI: Get student logs for a specific parent
    @GetMapping("/parent/logs/{parentId}")
    public ResponseEntity<?> getStudentLogsForParent(@PathVariable Integer parentId) {
        return ResponseEntity.status(200).body(exitLogService.studentLogForParent(parentId));
    }

    // link to UI: Get all logs for admin
    @GetMapping("/admin/logs")
    public ResponseEntity<?> getAllLogsForAdmin() {
        return ResponseEntity.status(200).body(exitLogService.findAllLogsForAdmin());
    }


}
