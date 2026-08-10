package com.ijp.candidateservice.mapper;

import com.ijp.candidateservice.dto.CandidateRequestDTO;
import com.ijp.candidateservice.dto.CandidateResponseDTO;
import com.ijp.candidateservice.dto.CandidateSummaryDTO;
import com.ijp.candidateservice.entity.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public Candidate toEntity(CandidateRequestDTO dto) {
        Candidate candidate = new Candidate();
        candidate.setFirstName(dto.getFirstName());
        candidate.setLastName(dto.getLastName());
        candidate.setEmployeeId(dto.getEmployeeId());
        candidate.setDateOfBirth(dto.getDateOfBirth());
        candidate.setEmail(dto.getEmail());
        candidate.setJobId(dto.getJobId());
        return candidate;
    }

    public CandidateResponseDTO toResponseDTO(Candidate candidate) {
        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(candidate.getId());
        dto.setFirstName(candidate.getFirstName());
        dto.setLastName(candidate.getLastName());
        dto.setEmployeeId(candidate.getEmployeeId());
        dto.setDateOfBirth(candidate.getDateOfBirth());
        dto.setEmail(candidate.getEmail());
        dto.setJobId(candidate.getJobId());
        return dto;
    }

    public CandidateSummaryDTO toSummaryDTO(Candidate candidate) {
        CandidateSummaryDTO dto = new CandidateSummaryDTO();
        dto.setId(candidate.getId());
        dto.setFirstName(candidate.getFirstName());
        dto.setLastName(candidate.getLastName());
        dto.setEmail(candidate.getEmail());
        return dto;
    }
}