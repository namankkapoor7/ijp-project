package com.ijp.jobservice.repository;

import com.ijp.jobservice.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    List<JobPosting> findByStatus(String status);

    List<JobPosting> findByStatusAndDesignation_TitleAndLocation(String status, String designationTitle, String location);

    List<JobPosting> findByStatusAndDesignation_Title(String status, String designationTitle);

    List<JobPosting> findByStatusAndLocation(String status, String location);
}