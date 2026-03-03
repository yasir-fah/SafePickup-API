package com.finalproject.safepickup.Service;

import com.finalproject.safepickup.Api.ApiException;
import com.finalproject.safepickup.DTOin.AdminDTO;
import com.finalproject.safepickup.Model.Admin;
import com.finalproject.safepickup.Model.User;
import com.finalproject.safepickup.Repository.AdminRepository;
import com.finalproject.safepickup.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    // 1- get All admins
    public List<Admin> findAll() {
        List<Admin> admins = adminRepository.findAll();
        return admins;
    }

    // 2- Register new admin
    public void registerAdmin(AdminDTO dto) {

        // 1- adding user attribute
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // todo: hashing at security
        user.setEmail(dto.getEmail());
        user.setRole("ADMIN");

        // 2- adding admin attribute
        Admin admin = new Admin();
        // LastLoginAt will be null initially

        // 3- link admin & user
        user.setAdmin(admin);
        admin.setUser(user);

        // 4- save
        userRepository.save(user);
        adminRepository.save(admin);
    }

    // 3- update an admin
    public void updateAdmin(Integer adminId, AdminDTO dto) {
        // 1- Find existing admin
        Admin oldAdmin = adminRepository.findAdminById(adminId);
        if(oldAdmin == null) {
            throw new ApiException("Admin not found");
        }

        // 2- Get the associated user
        User user = oldAdmin.getUser();
        if(user == null) {
            throw new ApiException("Associated user not found");
        }

        // 3- Update user fields
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // todo: hashing at security
        user.setEmail(dto.getEmail());

        // 4- Admin entity doesn't have fields to update (only LastLoginAt which is auto-managed)

        // 5- Save both (cascade should handle this, but being explicit)
        adminRepository.save(oldAdmin);
        userRepository.save(user);
    }

    // 4- delete an admin
    public void deleteAdmin(Integer adminId) {
        // 1- Find existing admin
        Admin admin = adminRepository.findAdminById(adminId);
        if(admin == null) {
            throw new ApiException("Admin not found");
        }

        // 2- delete
        User user = admin.getUser();
        userRepository.delete(user);
    }

}
