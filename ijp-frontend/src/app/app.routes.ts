import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Register } from './register/register';
import { AdminLogin } from './admin-login/admin-login';
import { AdminDashboard } from './admin-dashboard/admin-dashboard';
import { AddJob } from './add-job/add-job';
import { CandidateList } from './candidate-list/candidate-list';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'register/:id', component: Register },
  { path: 'admin/login', component: AdminLogin },
  { path: 'admin/dashboard', component: AdminDashboard, canActivate: [authGuard] },
  { path: 'admin/add-job', component: AddJob, canActivate: [authGuard] },
  { path: 'admin/candidates/:id', component: CandidateList, canActivate: [authGuard] },
];