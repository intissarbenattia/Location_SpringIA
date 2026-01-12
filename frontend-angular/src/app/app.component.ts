// ========== app.component.ts ==========
import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'SmartRent AI';
  isAuthenticated = false;
  isAdmin = false;
  currentUser: any = null;
  currentRoute = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Écouter les changements d'authentification
    this.authService.currentUser.subscribe(user => {
      this.currentUser = user;
      this.isAuthenticated = this.authService.isAuthenticated();
      this.isAdmin = this.authService.isAdmin();
    });

    // Écouter les changements de route
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.currentRoute = event.url;
    });
  }

  /**
   * Navigue vers le chat
   */
  goToChat(): void {
    this.router.navigate(['/chat']);
  }

  /**
   * Navigue vers les locations
   */
  goToLocations(): void {
    this.router.navigate(['/locations']);
  }

  /**
   * Navigue vers le dashboard des réservations
   */
  goToDashboard(): void {
    this.router.navigate(['/dashboard/reservations']);
  }

  /**
   * Vérifie si la route actuelle est active
   */
  isRouteActive(route: string): boolean {
    return this.currentRoute.includes(route);
  }

  /**
   * Déconnexion
   */
  logout(): void {
    if (confirm('Voulez-vous vraiment vous déconnecter ?')) {
      this.authService.logout();
    }
  }
}
