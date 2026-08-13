package com.ijp.candidateservice.repository;

import com.ijp.candidateservice.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findByJobId(Long jobId);

    boolean existsByEmailAndJobId(String email, Long jobId);

    boolean existsByEmployeeIdAndJobId(String employeeId, Long jobId);
}