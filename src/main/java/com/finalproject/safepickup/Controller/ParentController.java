package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.ExitRequestDTO;
import com.finalproject.safepickup.DTOin.ParentDTO;
import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/parent")
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    // ==================== ADMIN endpoints ====================

    @GetMapping("/get/parents")
    public ResponseEntity<?> getAllParent() {
        return ResponseEntity.status(200).body(parentService.findAll());
    }

    @DeleteMapping("/delete/parent/{parentId}")
    public ResponseEntity<?> deleteParent(@PathVariable Integer parentId) {
        parentService.deleteParent(parentId);
        return ResponseEntity.status(200).body(new ApiResponse("Parent deleted successfully"));
    }

    @GetMapping("/student/assignment")
    public ResponseEntity<?> findAllParentsForStudentAssignment() {
        return ResponseEntity.status(200).body(parentService.findAllParentsForStudentAssignment());
    }

    // ==================== PARENT endpoints (self-operations) ====================

    @PutMapping("/update")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal User user,
                                             @RequestBody @Valid ParentDTO dto) {
        parentService.updateParent(user.getId(), dto);
        return ResponseEntity.status(200).body(new ApiResponse("Parent updated successfully"));
    }

    @GetMapping("/congestion/overview/student/{studentId}")
    public ResponseEntity<?> getCongestionOverview(@AuthenticationPrincipal User user,
                                                   @PathVariable Integer studentId) {
        return ResponseEntity.status(200).body(
                parentService.getTrafficDataForParent(user.getId(), studentId));
    }

    @PostMapping("/exit/request/student/{studentId}")
    public ResponseEntity<?> parentExitRequest(@AuthenticationPrincipal User user,
                                               @PathVariable Integer studentId,
                                               @Valid @RequestBody ExitRequestDTO dto) {
        parentService.parentExitRequest(user.getId(), studentId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Parent exit requested successfully"));
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> askForOTP(@AuthenticationPrincipal User user) {
        String username = parentService.askForOtp(user.getId()).getUsername();
        return ResponseEntity.ok("OTP sent to " + username);
    }

    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<?> verifyOTP(@AuthenticationPrincipal User user,
                                       @PathVariable String otp) {
        parentService.verifyExitOTP(user.getId(), otp);
        return ResponseEntity.status(200).body(
                new ApiResponse("OTP verified! Exit request approved for 10 minutes")
        );
    }

    @PostMapping("/biometric-auth")
    public ResponseEntity<?> biometricAuth(@AuthenticationPrincipal User user) {
        parentService.verifyExitBiometric(user.getId());
        return ResponseEntity.status(200).body(
                new ApiResponse("Biometric verified! Exit request approved for 10 minutes")
        );
    }

}