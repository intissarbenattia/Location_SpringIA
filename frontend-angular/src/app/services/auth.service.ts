import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, tap, catchError, throwError } from 'rxjs';
import { LoginRequest, LoginResponse, User } from '../models/auth.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8888/api/auth';
  
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;
  
  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = this.getUserFromStorage();
    this.currentUserSubject = new BehaviorSubject<User | null>(storedUser);
    this.currentUser = this.currentUserSubject.asObservable();
    
    console.log('🔧 AuthService initialisé - API URL:', this.apiUrl);
  }
  
  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }
  
  public isAuthenticated(): boolean {
    return !!this.getToken();
  }

  public isAdmin(): boolean {
    const user = this.currentUserValue;
    return user ? user.roles.includes('ROLE_ADMIN') : false;
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }
  
  login(credentials: LoginRequest): Observable<LoginResponse> {
    console.log('🚀 Envoi requête login:', credentials.username);
    console.log('📡 URL:', `${this.apiUrl}/login`);

    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        console.log('✅ Réponse reçue:', response);

        localStorage.setItem('token', response.token);
        localStorage.setItem('username', response.username);
        localStorage.setItem('email', response.email);

        const roles = this.extractRolesFromToken(response.token);
        localStorage.setItem('roles', JSON.stringify(roles));

        const user: User = {
          username: response.username,
          email: response.email,
          roles: roles
        };
        this.currentUserSubject.next(user);

        console.log('✅ Utilisateur connecté');
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('❌ Erreur login:', {
          status: error.status,
          message: error.message,
          url: error.url
        });
        return throwError(() => error);
      })
    );
  }
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    localStorage.removeItem('roles');
    localStorage.removeItem('conversationId');

    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  private extractRolesFromToken(token: string): string[] {
    try {
      const payload = token.split('.')[1];
      const decodedPayload = JSON.parse(atob(payload));
      return decodedPayload.roles || [];
    } catch (error) {
      console.error('Erreur lors de l\'extraction des rôles du token:', error);
      return [];
    }
  }
  
  private getUserFromStorage(): User | null {
    const username = localStorage.getItem('username');
    const email = localStorage.getItem('email');
    const rolesString = localStorage.getItem('roles');

    if (username && email) {
      const roles = rolesString ? JSON.parse(rolesString) : [];
      return { username, email, roles };
    }

    return null;
  }
  
  validateToken(): Observable<any> {
    return this.http.get(`${this.apiUrl}/validate`);
  }
}