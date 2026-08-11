package com.ijp.jobservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class JobPostingRequestDTO {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Designation is required")
    private Long designationId;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Skill set is required")
    private String skillSet;

    @NotNull(message = "Experience years is required")
    private Integer experienceYears;

    @NotBlank(message = "Languages known is required")
    private String languagesKnown;

    @NotNull(message = "Minimum salary is required")
    private BigDecimal salaryMin;

    @NotNull(message = "Maximum salary is required")
    private BigDecimal salaryMax;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(String languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }
}