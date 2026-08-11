package com.ijp.jobservice.service;

import com.ijp.jobservice.dto.JobPostingRequestDTO;
import com.ijp.jobservice.dto.JobPostingResponseDTO;
import com.ijp.jobservice.entity.Designation;
import com.ijp.jobservice.entity.JobPosting;
import com.ijp.jobservice.mapper.JobPostingMapper;
import com.ijp.jobservice.repository.DesignationRepository;
import com.ijp.jobservice.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private JobPostingMapper mapper;

    public JobPostingResponseDTO createJobPosting(JobPostingRequestDTO requestDTO) {
        Designation designation = designationRepository.findById(requestDTO.getDesignationId())
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with id: " + requestDTO.getDesignationId()));

        JobPosting job = mapper.toEntity(requestDTO, designation);
        job.setJobCode(generateJobCode());
        job.setStatus("OPEN");
        job.setPostedDate(LocalDate.now());

        JobPosting saved = jobPostingRepository.save(job);
        return mapper.toResponseDTO(saved);
    }

    public List<JobPostingResponseDTO> getJobPostings(String designation, String location) {
        List<JobPosting> results;

        if (designation != null && location != null) {
            results = jobPostingRepository.findByStatusAndDesignation_TitleAndLocation("OPEN", designation, location);
        } else if (designation != null) {
            results = jobPostingRepository.findByStatusAndDesignation_Title("OPEN", designation);
        } else if (location != null) {
            results = jobPostingRepository.findByStatusAndLocation("OPEN", location);
        } else {
            results = jobPostingRepository.findByStatus("OPEN");
        }

        return results.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public JobPostingResponseDTO getJobById(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found with id: " + id));
        return mapper.toResponseDTO(job);
    }

    public JobPostingResponseDTO updateJobPosting(Long id, JobPostingRequestDTO requestDTO) {
        JobPosting existing = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found with id: " + id));

        Designation designation = designationRepository.findById(requestDTO.getDesignationId())
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with id: " + requestDTO.getDesignationId()));

        existing.setDescription(requestDTO.getDescription());
        existing.setDesignation(designation);
        existing.setLocation(requestDTO.getLocation());
        existing.setSkillSet(requestDTO.getSkillSet());
        existing.setExperienceYears(requestDTO.getExperienceYears());
        existing.setLanguagesKnown(requestDTO.getLanguagesKnown());
        existing.setSalaryMin(requestDTO.getSalaryMin());
        existing.setSalaryMax(requestDTO.getSalaryMax());

        JobPosting saved = jobPostingRepository.save(existing);
        return mapper.toResponseDTO(saved);
    }

    public JobPostingResponseDTO closeJobPosting(Long id) {
        JobPosting job = jobPostingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found with id: " + id));
        job.setStatus("CLOSED");
        JobPosting saved = jobPostingRepository.save(job);
        return mapper.toResponseDTO(saved);
    }

    private String generateJobCode() {
        return "JOB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}