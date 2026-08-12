import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Job, JobPostingResponse } from '../services/job';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {

  jobs: JobPostingResponse[] = [];
  designationFilter = '';
  locationFilter = '';
  loading = false;
  errorMessage = '';

  constructor(private jobService: Job) { }

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.loading = true;
    this.errorMessage = '';

    this.jobService.getJobs(this.designationFilter || undefined, this.locationFilter || undefined)
      .subscribe({
        next: (data) => {
          this.jobs = data;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Unable to load job postings. Please try again later.';
          this.loading = false;
        }
      });
  }

  onFilterChange(): void {
    this.loadJobs();
  }
}