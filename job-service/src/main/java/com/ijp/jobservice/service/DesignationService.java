package com.ijp.jobservice.service;

import com.ijp.jobservice.dto.DesignationDTO;
import com.ijp.jobservice.entity.Designation;
import com.ijp.jobservice.mapper.JobPostingMapper;
import com.ijp.jobservice.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignationService {

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private JobPostingMapper mapper;

    public DesignationDTO addDesignation(DesignationDTO dto) {
        Designation designation = mapper.toDesignationEntity(dto);
        Designation saved = designationRepository.save(designation);
        return mapper.toDesignationDTO(saved);
    }

    public List<DesignationDTO> getAllDesignations() {
        return designationRepository.findAll()
                .stream()
                .map(mapper::toDesignationDTO)
                .collect(Collectors.toList());
    }
}