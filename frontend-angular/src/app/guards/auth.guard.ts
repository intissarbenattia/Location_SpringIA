import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard d'authentification (Angular 17+ Functional Guard)
 * Protège les routes qui nécessitent une authentification
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Vérifier si l'utilisateur est authentifié
  if (authService.isAuthenticated()) {
    return true;
  }
  
  // Rediriger vers la page de connexion si non authentifié
  // Conserver l'URL demandée pour redirection après connexion
  router.navigate(['/login'], {
    queryParams: { returnUrl: state.url }
  });
  
  return false;
};

/**
 * Guard pour empêcher l'accès à la page de login si déjà connecté
 */
export const loginGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Si l'utilisateur est déjà connecté, rediriger vers le chat
  if (authService.isAuthenticated()) {
    router.navigate(['/chat']);
    return false;
  }
  
  return true;
};