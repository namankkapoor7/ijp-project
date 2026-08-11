package com.ijp.candidateservice.service;

import com.ijp.candidateservice.client.JobClient;
import com.ijp.candidateservice.dto.CandidateRequestDTO;
import com.ijp.candidateservice.dto.CandidateResponseDTO;
import com.ijp.candidateservice.dto.CandidateSummaryDTO;
import com.ijp.candidateservice.dto.JobSummaryDTO;
import com.ijp.candidateservice.entity.Candidate;
import com.ijp.candidateservice.exception.ServiceUnavailableException;
import com.ijp.candidateservice.mapper.CandidateMapper;
import com.ijp.candidateservice.repository.CandidateRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateMapper candidateMapper;

    @Autowired
    private JobClient jobClient;

    public CandidateResponseDTO registerCandidate(CandidateRequestDTO requestDTO) {
        validateJobIsOpen(requestDTO.getJobId());

        boolean alreadyApplied = candidateRepository.existsByEmailAndJobId(
                requestDTO.getEmail(), requestDTO.getJobId());

        if (alreadyApplied) {
            throw new IllegalStateException("This email has already applied for this job");
        }

        Candidate candidate = candidateMapper.toEntity(requestDTO);
        Candidate saved = candidateRepository.save(candidate);
        return candidateMapper.toResponseDTO(saved);
    }

    private void validateJobIsOpen(Long jobId) {
        JobSummaryDTO job;
        try {
            job = jobClient.getJobById(jobId);
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Job posting not found with id: " + jobId);
        } catch (FeignException ex) {
            throw new ServiceUnavailableException("Unable to verify job posting. Please try again later.");
        }

        if (!"OPEN".equalsIgnoreCase(job.getStatus())) {
            throw new IllegalStateException("Cannot apply: this job posting is closed");
        }
    }

    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll()
                .stream()
                .map(candidateMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CandidateSummaryDTO> getCandidatesByJobId(Long jobId) {
        return candidateRepository.findByJobId(jobId)
                .stream()
                .map(candidateMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }
}