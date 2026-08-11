package com.ijp.jobservice.repository;

import com.ijp.jobservice.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
}