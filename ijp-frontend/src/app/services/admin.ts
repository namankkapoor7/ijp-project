import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AdminLoginRequest {
  email: string;
  password: string;
}

export interface AdminLoginResponse {
  success: boolean;
  email: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class Admin {

  private baseUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) { }

  login(credentials: AdminLoginRequest): Observable<AdminLoginResponse> {
    return this.http.post<AdminLoginResponse>(`${this.baseUrl}/login`, credentials);
  }
}