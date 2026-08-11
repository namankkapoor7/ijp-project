package com.ijp.adminservice.service;

import com.ijp.adminservice.dto.AdminLoginRequestDTO;
import com.ijp.adminservice.dto.AdminLoginResponseDTO;
import com.ijp.adminservice.entity.Admin;
import com.ijp.adminservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminLoginResponseDTO login(AdminLoginRequestDTO requestDTO) {
        Admin admin = adminRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(requestDTO.getPassword(), admin.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        AdminLoginResponseDTO response = new AdminLoginResponseDTO();
        response.setSuccess(true);
        response.setEmail(admin.getEmail());
        response.setMessage("Login successful");
        return response;
    }

    // used once, to seed an admin account for testing — see verification steps below
    public void registerAdmin(String email, String rawPassword) {
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        adminRepository.save(admin);
    }
}