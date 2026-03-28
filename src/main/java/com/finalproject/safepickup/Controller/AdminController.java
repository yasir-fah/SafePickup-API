package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.AdminDTO;
import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get/admins")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.status(200).body(adminService.findAll());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal User user,
                                             @RequestBody @Valid AdminDTO dto) {
        adminService.updateAdmin(user.getId(), dto);
        return ResponseEntity.status(200).body(new ApiResponse("Admin updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMyAccount(@AuthenticationPrincipal User user) {
        adminService.deleteAdmin(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Admin deleted successfully"));
    }
}
