import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Candidate, CandidateRequest } from '../services/candidate';
import { Job, JobPostingResponse } from '../services/job';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register implements OnInit {

  jobId!: number;
  job: JobPostingResponse | null = null;

  firstName = '';
  lastName = '';
  employeeId = '';
  dateOfBirth = '';
  email = '';

  submitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private candidateService: Candidate,
    private jobService: Job
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.jobId = Number(idParam);

    this.jobService.getJobById(this.jobId).subscribe({
      next: (data) => {
        this.job = data;
      },
      error: () => {
        this.errorMessage = 'This job posting could not be found.';
      }
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.firstName || !this.lastName || !this.employeeId || !this.dateOfBirth || !this.email) {
      this.errorMessage = 'All fields are required.';
      return;
    }

    const request: CandidateRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      employeeId: this.employeeId,
      dateOfBirth: this.dateOfBirth,
      email: this.email,
      jobId: this.jobId
    };

    this.submitting = true;

    this.candidateService.register(request).subscribe({
      next: () => {
        this.successMessage = 'Details have been saved successfully.';
        this.submitting = false;
        setTimeout(() => this.router.navigate(['/']), 1500);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error || 'Something went wrong. Please try again.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }
}