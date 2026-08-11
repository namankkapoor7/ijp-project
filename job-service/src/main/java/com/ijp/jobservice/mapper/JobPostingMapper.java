package com.ijp.jobservice.mapper;

import com.ijp.jobservice.dto.DesignationDTO;
import com.ijp.jobservice.dto.JobPostingRequestDTO;
import com.ijp.jobservice.dto.JobPostingResponseDTO;
import com.ijp.jobservice.entity.Designation;
import com.ijp.jobservice.entity.JobPosting;
import org.springframework.stereotype.Component;

@Component
public class JobPostingMapper {

    public JobPosting toEntity(JobPostingRequestDTO dto, Designation designation) {
        JobPosting job = new JobPosting();
        job.setDescription(dto.getDescription());
        job.setDesignation(designation);
        job.setLocation(dto.getLocation());
        job.setSkillSet(dto.getSkillSet());
        job.setExperienceYears(dto.getExperienceYears());
        job.setLanguagesKnown(dto.getLanguagesKnown());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        return job;
    }

    public JobPostingResponseDTO toResponseDTO(JobPosting job) {
        JobPostingResponseDTO dto = new JobPostingResponseDTO();
        dto.setId(job.getId());
        dto.setJobCode(job.getJobCode());
        dto.setDescription(job.getDescription());
        dto.setDesignationTitle(job.getDesignation().getTitle());
        dto.setLocation(job.getLocation());
        dto.setSkillSet(job.getSkillSet());
        dto.setExperienceYears(job.getExperienceYears());
        dto.setLanguagesKnown(job.getLanguagesKnown());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax());
        dto.setStatus(job.getStatus());
        dto.setPostedDate(job.getPostedDate());
        return dto;
    }

    public DesignationDTO toDesignationDTO(Designation designation) {
        DesignationDTO dto = new DesignationDTO();
        dto.setId(designation.getId());
        dto.setTitle(designation.getTitle());
        return dto;
    }

    public Designation toDesignationEntity(DesignationDTO dto) {
        Designation designation = new Designation();
        designation.setTitle(dto.getTitle());
        return designation;
    }
}