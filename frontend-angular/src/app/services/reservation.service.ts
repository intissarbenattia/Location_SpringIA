import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Reservation {
  id: string;
  locationId: string;
  locationName: string;
  userId: string;
  userName: string;
  status: 'CONFIRMED' | 'PENDING' | 'CANCELLED';
  checkIn: string;
  checkOut: string;
  price: number;
  createdAt: string;
}

export interface ReservationStats {
  total: number;
  confirmed: number;
  pending: number;
  cancelled: number;
  revenue: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private apiUrl = 'http://localhost:8888/api/reservations';

  constructor(private http: HttpClient) {}

  /**
   * Récupère toutes les réservations
   */
  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.apiUrl);
  }

  /**
   * Récupère les statistiques des réservations
   */
  getStats(): Observable<ReservationStats> {
    return this.http.get<ReservationStats>(`${this.apiUrl}/stats`);
  }

  /**
   * Récupère les réservations récentes
   */
  getRecentReservations(limit: number = 5): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}?limit=${limit}`);
  }

  /**
   * Récupère les réservations par statut
   */
  getReservationsByStatus(status: string): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}?status=${status}`);
  }
}
