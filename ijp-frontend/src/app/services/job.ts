import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CandidateSummary } from './candidate';

export interface JobPostingRequest {
  description: string;
  designationId: number;
  location: string;
  skillSet: string;
  experienceYears: number;
  languagesKnown: string;
  salaryMin: number;
  salaryMax: number;
}

export interface JobPostingResponse {
  id: number;
  jobCode: string;
  description: string;
  designationTitle: string;
  location: string;
  skillSet: string;
  experienceYears: number;
  languagesKnown: string;
  salaryMin: number;
  salaryMax: number;
  status: string;
  postedDate: string;
}

export interface Designation {
  id: number;
  title: string;
}

export interface JobApplicationNotification {
  jobId: number;
  jobCode: string;
  unseenCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class Job {

  private baseUrl = 'http://localhost:8080/api/jobs';

  constructor(private http: HttpClient) { }

  createJob(job: JobPostingRequest): Observable<JobPostingResponse> {
    return this.http.post<JobPostingResponse>(this.baseUrl, job);
  }

  getJobs(designation?: string, location?: string): Observable<JobPostingResponse[]> {
    let params = new HttpParams();
    if (designation) {
      params = params.set('designation', designation);
    }
    if (location) {
      params = params.set('location', location);
    }
    return this.http.get<JobPostingResponse[]>(this.baseUrl, { params });
  }

  getJobById(id: number): Observable<JobPostingResponse> {
    return this.http.get<JobPostingResponse>(`${this.baseUrl}/${id}`);
  }

  updateJob(id: number, job: JobPostingRequest): Observable<JobPostingResponse> {
    return this.http.put<JobPostingResponse>(`${this.baseUrl}/${id}`, job);
  }

  closeJob(id: number): Observable<JobPostingResponse> {
    return this.http.put<JobPostingResponse>(`${this.baseUrl}/${id}/close`, {});
  }

  getDesignations(): Observable<Designation[]> {
    return this.http.get<Designation[]>(`${this.baseUrl}/designations`);
  }

  addDesignation(designation: { title: string }): Observable<Designation> {
    return this.http.post<Designation>(`${this.baseUrl}/designations`, designation);
  }

  getCandidatesForJob(jobId: number): Observable<CandidateSummary[]> {
    return this.http.get<CandidateSummary[]>(`${this.baseUrl}/${jobId}/candidates`);
  }

  getUnseenApplications(): Observable<JobApplicationNotification[]> {
    return this.http.get<JobApplicationNotification[]>(`${this.baseUrl}/notifications/unseen-applications`);
  }

  markApplicationsSeen(jobId: number): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${jobId}/notifications/mark-seen`, {});
  }
}