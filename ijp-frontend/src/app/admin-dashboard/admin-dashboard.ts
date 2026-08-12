import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Job, JobPostingResponse, JobApplicationNotification } from '../services/job';
import { Auth } from '../services/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboard implements OnInit {

  jobs: JobPostingResponse[] = [];
  notifications: JobApplicationNotification[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private jobService: Job,
    private authService: Auth,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadJobs();
    this.loadNotifications();
  }

  loadJobs(): void {
    this.loading = true;
    this.jobService.getAllJobsForAdmin().subscribe({
      next: (data) => {
        this.jobs = data;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Unable to load job postings.';
        this.loading = false;
      }
    });
  }

  loadNotifications(): void {
    this.jobService.getUnseenApplications().subscribe({
      next: (data) => {
        this.notifications = data;
      },
      error: () => {
        // notification bar failing quietly is acceptable — not core functionality
      }
    });
  }

  unseenCountFor(jobId: number): number {
    const match = this.notifications.find(n => n.jobId === jobId);
    return match ? match.unseenCount : 0;
  }

  closeJob(jobId: number): void {
    this.jobService.closeJob(jobId).subscribe({
      next: () => this.loadJobs(),
      error: () => {
        this.errorMessage = 'Unable to close job posting.';
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}