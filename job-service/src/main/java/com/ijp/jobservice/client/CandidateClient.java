package com.ijp.jobservice.client;

import com.ijp.jobservice.dto.CandidateSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "candidate-service")
public interface CandidateClient {

    @GetMapping("/api/candidates/job/{jobId}")
    List<CandidateSummaryDTO> getCandidatesByJob(@PathVariable Long jobId);
}