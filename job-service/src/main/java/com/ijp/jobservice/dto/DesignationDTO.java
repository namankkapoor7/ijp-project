package com.ijp.jobservice.dto;

import jakarta.validation.constraints.NotBlank;

public class DesignationDTO {

    private Long id;

    @NotBlank(message = "Designation title is required")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}