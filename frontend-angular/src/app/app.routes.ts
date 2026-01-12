import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ChatComponent } from './components/chat/chat.component';
import { LocationsComponent } from './components/locations/locations.component';
import { ReservationsDashboardComponent } from './components/reservations-dashboard/reservations-dashboard.component';
import { authGuard, loginGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

/**
 * Configuration des routes de l'application
 */
export const routes: Routes = [
  // Route par défaut : redirection vers login
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },

  // Route de connexion (accessible uniquement si non connecté)
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [loginGuard]
  },

  // Route du chat (accessible uniquement si connecté)
  {
    path: 'chat',
    component: ChatComponent,
    canActivate: [authGuard]
  },

  // Route de gestion des locations (accessible uniquement aux admins)
  {
    path: 'locations',
    component: LocationsComponent,
    canActivate: [authGuard, adminGuard]
  },

  // Route du dashboard des réservations (accessible uniquement aux admins)
  {
    path: 'dashboard/reservations',
    component: ReservationsDashboardComponent,
    canActivate: [authGuard, adminGuard]
  },

  // Route 404 : redirection vers login
  {
    path: '**',
    redirectTo: '/login'
  }
];