package com.ijp.candidateservice.client;

import com.ijp.candidateservice.dto.JobSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    JobSummaryDTO getJobById(@PathVariable("id") Long id);
}