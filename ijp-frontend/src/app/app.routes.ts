import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Register } from './register/register';
import { AdminLogin } from './admin-login/admin-login';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'register/:id', component: Register },
  { path: 'admin/login', component: AdminLogin },
];