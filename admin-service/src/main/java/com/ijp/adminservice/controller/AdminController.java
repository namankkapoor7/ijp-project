package com.ijp.adminservice.controller;

import com.ijp.adminservice.dto.AdminLoginRequestDTO;
import com.ijp.adminservice.dto.AdminLoginResponseDTO;
import com.ijp.adminservice.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDTO> login(@Valid @RequestBody AdminLoginRequestDTO requestDTO) {
        return ResponseEntity.ok(adminService.login(requestDTO));
    }

    // temporary helper to seed a test admin — remove before final submission
    @PostMapping("/seed")
    public ResponseEntity<String> seedAdmin(@RequestParam String email, @RequestParam String password) {
        adminService.registerAdmin(email, password);
        return new ResponseEntity<>("Admin seeded", HttpStatus.CREATED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidLogin(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}