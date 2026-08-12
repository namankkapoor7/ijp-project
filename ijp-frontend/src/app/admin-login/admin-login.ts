import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Admin } from '../services/admin';
import { Auth } from '../services/auth';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-login.html',
  styleUrl: './admin-login.css'
})
export class AdminLogin {

  email = '';
  password = '';
  submitting = false;
  errorMessage = '';

  constructor(
    private adminService: Admin,
    private authService: Auth,
    private router: Router
  ) { }

  onSubmit(): void {
    this.errorMessage = '';

    if (!this.email || !this.password) {
      this.errorMessage = 'Email and password are required.';
      return;
    }

    this.submitting = true;

    this.adminService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        this.submitting = false;
        this.authService.login(response.email);
        this.router.navigate(['/admin/dashboard']);
      },
      error: (err) => {
        this.submitting = false;
        this.errorMessage = err.error || 'Login failed. Please try again.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }
}