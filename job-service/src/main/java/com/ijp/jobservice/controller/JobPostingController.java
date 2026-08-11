package com.ijp.jobservice.controller;

import com.ijp.jobservice.dto.JobApplicationNotificationDTO;
import com.ijp.jobservice.dto.CandidateSummaryDTO;
import com.ijp.jobservice.dto.JobPostingRequestDTO;
import com.ijp.jobservice.dto.JobPostingResponseDTO;
import com.ijp.jobservice.service.JobPostingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @PostMapping
    public ResponseEntity<JobPostingResponseDTO> createJob(@Valid @RequestBody JobPostingRequestDTO requestDTO) {
        return new ResponseEntity<>(jobPostingService.createJobPosting(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobPostingResponseDTO>> getJobs(
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(jobPostingService.getJobPostings(designation, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostingResponseDTO> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostingService.getJobById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostingResponseDTO> updateJob(@PathVariable Long id, @Valid @RequestBody JobPostingRequestDTO requestDTO) {
        return ResponseEntity.ok(jobPostingService.updateJobPosting(id, requestDTO));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<JobPostingResponseDTO> closeJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostingService.closeJobPosting(id));
    }

    @GetMapping("/{id}/candidates")
    public ResponseEntity<List<CandidateSummaryDTO>> getCandidatesForJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobPostingService.getCandidatesForJob(id));
    }

    @GetMapping("/notifications/unseen-applications")
    public ResponseEntity<List<JobApplicationNotificationDTO>> getUnseenApplications() {
        return ResponseEntity.ok(jobPostingService.getUnseenApplicationNotifications());
    }

    @PutMapping("/{id}/notifications/mark-seen")
    public ResponseEntity<Void> markSeen(@PathVariable Long id) {
        jobPostingService.markApplicationsSeen(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleServiceUnavailable(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

}