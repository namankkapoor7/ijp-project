import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Job, JobPostingResponse } from '../services/job';
import { CandidateSummary } from '../services/candidate';

@Component({
  selector: 'app-candidate-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './candidate-list.html',
  styleUrl: './candidate-list.css'
})
export class CandidateList implements OnInit {

  jobId!: number;
  job: JobPostingResponse | null = null;
  candidates: CandidateSummary[] = [];
  loading = false;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private jobService: Job
  ) { }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.jobId = Number(idParam);

    this.jobService.getJobById(this.jobId).subscribe({
      next: (data) => this.job = data
    });

    this.loadCandidates();
  }

  loadCandidates(): void {
    this.loading = true;
    this.errorMessage = '';

    this.jobService.getCandidatesForJob(this.jobId).subscribe({
      next: (data) => {
        this.candidates = data;
        this.loading = false;
        this.jobService.markApplicationsSeen(this.jobId).subscribe();
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error || 'Unable to load candidates for this job.';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/dashboard']);
  }
}