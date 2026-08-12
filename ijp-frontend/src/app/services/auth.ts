import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private loggedInEmail: string | null = null;

  login(email: string): void {
    this.loggedInEmail = email;
  }

  logout(): void {
    this.loggedInEmail = null;
  }

  isLoggedIn(): boolean {
    return this.loggedInEmail !== null;
  }

  getEmail(): string | null {
    return this.loggedInEmail;
  }
}