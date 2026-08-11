package com.ijp.candidateservice.controller;

import com.ijp.candidateservice.dto.CandidateRequestDTO;
import com.ijp.candidateservice.dto.CandidateResponseDTO;
import com.ijp.candidateservice.dto.CandidateSummaryDTO;
import com.ijp.candidateservice.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponseDTO> registerCandidate(@Valid @RequestBody CandidateRequestDTO requestDTO) {
        CandidateResponseDTO response = candidateService.registerCandidate(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<CandidateSummaryDTO>> getCandidatesByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(candidateService.getCandidatesByJobId(jobId));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleDuplicateApplication(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(com.ijp.candidateservice.exception.ServiceUnavailableException.class)
    public ResponseEntity<String> handleServiceUnavailable(com.ijp.candidateservice.exception.ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

}