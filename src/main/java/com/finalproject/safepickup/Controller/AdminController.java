package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.DTOin.AdminDTO;
import com.finalproject.safepickup.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public ResponseEntity<?> adminRegister(@Valid @RequestBody AdminDTO adminDTO) {
        adminService.registerAdmin(adminDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Admin successfully registered!"));
    }

    @PutMapping("/update/admin/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId,
                                         @RequestBody @Valid AdminDTO dto) {
        adminService.updateAdmin(adminId, dto);
        return ResponseEntity.status(200).body(new ApiResponse("Admin updated successfully"));
    }

    @DeleteMapping("/delete/admin/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(200).body(new ApiResponse("Admin deleted successfully"));
    }
}