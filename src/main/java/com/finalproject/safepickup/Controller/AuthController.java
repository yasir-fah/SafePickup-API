package com.finalproject.safepickup.Controller;

import com.finalproject.safepickup.Api.ApiResponse;
import com.finalproject.safepickup.Api.AuthToken;
import com.finalproject.safepickup.DTOin.AdminDTO;
import com.finalproject.safepickup.DTOin.LoginDTO;
import com.finalproject.safepickup.DTOin.ParentDTO;
import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Service.AdminService;
import com.finalproject.safepickup.Service.JwtService;
import com.finalproject.safepickup.Service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminService adminService;
    private final ParentService parentService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        // Update last login for admins
        if (user.getAdmin() != null) {
            user.getAdmin().setLastLoginAt(java.time.LocalDateTime.now());
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDTO adminDTO) {
        adminService.registerAdmin(adminDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Admin successfully registered!"));
    }

    @PostMapping("/register/parent")
    public ResponseEntity<?> registerParent(@Valid @RequestBody ParentDTO parentDTO) {
        parentService.registerParent(parentDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Parent registered successfully!"));
    }
}