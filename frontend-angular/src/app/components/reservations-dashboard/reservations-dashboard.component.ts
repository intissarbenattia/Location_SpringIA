import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservationService, ReservationStats, Reservation } from '../../services/reservation.service';

@Component({
  selector: 'app-reservations-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reservations-dashboard.component.html',
  styleUrls: ['./reservations-dashboard.component.scss']
})
export class ReservationsDashboardComponent implements OnInit {
  stats: ReservationStats = {
    total: 0,
    confirmed: 0,
    pending: 0,
    cancelled: 0,
    revenue: 0
  };

  recentReservations: Reservation[] = [];
  loading = true;
  error: string | null = null;

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadRecentReservations();
  }

  /**
   * Charge les statistiques des réservations
   */
  loadStats(): void {
    this.reservationService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des statistiques:', err);
        this.error = 'Impossible de charger les statistiques';
        this.loading = false;
        // Mock data for development
        this.stats = {
          total: 48,
          confirmed: 32,
          pending: 8,
          cancelled: 8,
          revenue: 12500
        };
      }
    });
  }

  /**
   * Charge les réservations récentes
   */
  loadRecentReservations(): void {
    this.reservationService.getRecentReservations(10).subscribe({
      next: (data) => {
        this.recentReservations = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des réservations:', err);
        // Mock data for development
        this.recentReservations = [
          {
            id: '1',
            locationId: 'loc-1',
            locationName: 'Penthouse Downtown',
            userId: 'user-1',
            userName: 'Jean Dupont',
            status: 'CONFIRMED',
            checkIn: '2026-01-10',
            checkOut: '2026-01-15',
            price: 750,
            createdAt: '2026-01-07'
          },
          {
            id: '2',
            locationId: 'loc-2',
            locationName: 'Studio Moderne',
            userId: 'user-2',
            userName: 'Marie Bernard',
            status: 'PENDING',
            checkIn: '2026-01-12',
            checkOut: '2026-01-14',
            price: 400,
            createdAt: '2026-01-07'
          },
          {
            id: '3',
            locationId: 'loc-3',
            locationName: 'Villa Côté Mer',
            userId: 'user-3',
            userName: 'Pierre Martin',
            status: 'CONFIRMED',
            checkIn: '2026-01-20',
            checkOut: '2026-01-27',
            price: 1200,
            createdAt: '2026-01-06'
          }
        ];
      }
    });
  }

  /**
   * Retourne la couleur du badge de statut
   */
  getStatusClass(status: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'status-confirmed';
      case 'PENDING':
        return 'status-pending';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return 'status-default';
    }
  }

  /**
   * Formate le prix
   */
  formatPrice(price: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(price);
  }

  /**
   * Formate la date
   */
  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR');
  }
}
