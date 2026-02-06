package com.example.matrimony.admin;

import com.example.matrimony.entity.Admin;
import com.example.matrimony.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminManagementService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminManagementService(AdminRepository adminRepository,
                                  PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------------------------
    // CREATE ADMIN
    // ------------------------------------
    public void createAdmin(CreateAdminRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        if (adminRepository.findByEmailId(request.getEmailId()).isPresent()) {
            throw new IllegalArgumentException("Admin already exists");
        }

        if ("SUPER_ADMIN".equals(request.getAdminRole())) {
            throw new IllegalArgumentException("Cannot create SUPER_ADMIN");
        }

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setEmailId(request.getEmailId());
        admin.setCreatePassword(passwordEncoder.encode(request.getPassword()));
        admin.setAdminRole(request.getAdminRole());

        adminRepository.save(admin);
    }

    // ------------------------------------
    // GET ALL ADMINS
    // ------------------------------------
    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {

        return adminRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ------------------------------------
    // DELETE ADMIN
    // ------------------------------------
    @Transactional
    public void deleteAdmin(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Admin not found"));

        if ("SUPER_ADMIN".equals(admin.getAdminRole())) {
            throw new IllegalArgumentException("SUPER_ADMIN cannot be deleted");
        }

        adminRepository.delete(admin);
    }

    // ------------------------------------
    // MAPPER
    // ------------------------------------
    private AdminResponse mapToResponse(Admin admin) {
        AdminResponse res = new AdminResponse();
        res.setAdminId(admin.getAdminId());
        res.setUsername(admin.getUsername());
        res.setEmailId(admin.getEmailId());
        res.setAdminRole(admin.getAdminRole());
        return res;
    }
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {

        Admin admin = adminRepository.findByEmailId(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Admin not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                admin.getCreatePassword())) {

            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        admin.setCreatePassword(
                passwordEncoder.encode(request.getNewPassword()));

        adminRepository.save(admin);
    }

}
