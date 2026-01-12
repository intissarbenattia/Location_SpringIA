import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard d'administration
 * Protège les routes qui nécessitent des droits d'administrateur
 */
export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Vérifier si l'utilisateur est authentifié et a le rôle admin
  if (authService.isAuthenticated() && authService.isAdmin()) {
    return true;
  }

  // Rediriger vers le chat si authentifié mais pas admin
  if (authService.isAuthenticated()) {
    router.navigate(['/chat']);
  } else {
    // Rediriger vers la page de connexion si non authentifié
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
  }

  return false;
};