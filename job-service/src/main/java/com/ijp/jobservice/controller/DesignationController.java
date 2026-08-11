package com.ijp.jobservice.controller;

import com.ijp.jobservice.dto.DesignationDTO;
import com.ijp.jobservice.service.DesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs/designations")
public class DesignationController {

    @Autowired
    private DesignationService designationService;

    @PostMapping
    public ResponseEntity<DesignationDTO> addDesignation(@Valid @RequestBody DesignationDTO dto) {
        return new ResponseEntity<>(designationService.addDesignation(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DesignationDTO>> getAllDesignations() {
        return ResponseEntity.ok(designationService.getAllDesignations());
    }
}