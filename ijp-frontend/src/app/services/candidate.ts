import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CandidateRequest {
  firstName: string;
  lastName: string;
  employeeId: string;
  dateOfBirth: string;
  email: string;
  jobId: number;
}

export interface CandidateResponse {
  id: number;
  firstName: string;
  lastName: string;
  employeeId: string;
  dateOfBirth: string;
  email: string;
  jobId: number;
}

export interface CandidateSummary {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class Candidate {

  private baseUrl = 'http://localhost:8080/api/candidates';

  constructor(private http: HttpClient) { }

  register(candidate: CandidateRequest): Observable<CandidateResponse> {
    return this.http.post<CandidateResponse>(this.baseUrl, candidate);
  }

  getCandidatesByJob(jobId: number): Observable<CandidateSummary[]> {
    return this.http.get<CandidateSummary[]>(`${this.baseUrl}/job/${jobId}`);
  }
}