import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Job, JobPostingRequest, Designation } from '../services/job';

@Component({
  selector: 'app-add-job',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './add-job.html',
  styleUrl: './add-job.css'
})
export class AddJob implements OnInit {

  designations: Designation[] = [];
  newDesignationTitle = '';

  description = '';
  designationId: number | null = null;
  location = '';
  skillSet = '';
  experienceYears: number | null = null;
  languagesKnown = '';
  salaryMin: number | null = null;
  salaryMax: number | null = null;

  submitting = false;
  errorMessage = '';
  successMessage = '';

  constructor(private jobService: Job, private router: Router) { }

  ngOnInit(): void {
    this.loadDesignations();
  }

  loadDesignations(): void {
    this.jobService.getDesignations().subscribe({
      next: (data) => this.designations = data,
      error: () => this.errorMessage = 'Unable to load designations.'
    });
  }

  addDesignation(): void {
    if (!this.newDesignationTitle.trim()) {
      return;
    }

    this.jobService.addDesignation({ title: this.newDesignationTitle }).subscribe({
      next: (created) => {
        this.designations.push(created);
        this.designationId = created.id;
        this.newDesignationTitle = '';
      },
      error: () => this.errorMessage = 'Unable to add designation.'
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.description || !this.designationId || !this.location || !this.skillSet ||
        !this.experienceYears || !this.languagesKnown || !this.salaryMin || !this.salaryMax) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    if (this.salaryMin > this.salaryMax) {
      this.errorMessage = 'Minimum salary cannot exceed maximum salary.';
      return;
    }

    const request: JobPostingRequest = {
      description: this.description,
      designationId: this.designationId,
      location: this.location,
      skillSet: this.skillSet,
      experienceYears: this.experienceYears,
      languagesKnown: this.languagesKnown,
      salaryMin: this.salaryMin,
      salaryMax: this.salaryMax
    };

    this.submitting = true;

    this.jobService.createJob(request).subscribe({
      next: () => {
        this.successMessage = 'New job posted successfully.';
        this.submitting = false;
        setTimeout(() => this.router.navigate(['/admin/dashboard']), 1500);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error || 'Unable to create job posting.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/admin/dashboard']);
  }
}