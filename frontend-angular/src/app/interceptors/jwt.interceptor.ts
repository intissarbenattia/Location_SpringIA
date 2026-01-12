import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

/**
 * Intercepteur JWT (Angular 17+)
 * - Ajoute automatiquement le token JWT
 * - Ignore login / register
 * - Déconnecte l'utilisateur en cas de 401
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // 🔹 NE PAS intercepter les endpoints publics
  if (req.url.includes('/api/auth/login') || req.url.includes('/api/auth/register')) {
    return next(req);
  }

  const token = authService.getToken();

  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      })
    : req;

  return next(authReq).pipe(
    catchError(error => {
      // 🔴 401 = token expiré / invalide
      if (error.status === 401) {
        console.warn('❌ Token invalide ou expiré → logout');

        // ⚠️ éviter boucle infinie
        authService.logout();

        if (router.url !== '/login') {
          router.navigate(['/login']);
        }
      }

      return throwError(() => error);
    })
  );
};
