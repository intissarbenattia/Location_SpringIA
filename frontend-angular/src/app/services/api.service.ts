import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl || 'http://localhost:8888/api';

  constructor(private http: HttpClient) {}

  /**
   * ==================== LOCATIONS ====================
   */

  /**
   * Récupère toutes les locations
   * MCP Tool: rechercher_locations
   */
  getLocations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/locations`);
  }

  /**
   * Récupère les locations disponibles
   * MCP Tool: rechercher_locations_disponibles
   */
  getAvailableLocations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/locations/available`);
  }

  /**
   * Récupère les locations par type
   * MCP Tool: rechercher_par_type
   */
  getLocationsByType(type: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/locations/type/${type}`);
  }

  /**
   * Récupère les locations disponibles par type
   * MCP Tool: rechercher_disponibles_par_type
   */
  getAvailableLocationsByType(type: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/locations/available/${type}`);
  }

  /**
   * Récupère les détails d'une location
   * MCP Tool: details_location
   */
  getLocationDetails(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/locations/${id}`);
  }

  /**
   * Calcule le prix d'une location
   * MCP Tool: calculer_prix_location
   */
  calculateLocationPrice(locationId: number, days: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/locations/${locationId}/price?days=${days}`);
  }

  /**
   * ==================== RESERVATIONS ====================
   */

  /**
   * Récupère toutes les réservations
   * MCP Tool: rechercher_reservations
   */
  getReservations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations`);
  }

  /**
   * Récupère les réservations confirmées
   * MCP Tool: rechercher_reservations_confirmees
   */
  getConfirmedReservations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations/statut/CONFIRMÉE`);
  }

  /**
   * Recherche les réservations d'un client
   * MCP Tool: rechercher_reservations_client
   */
  searchReservationsByClient(email: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations/client/${encodeURIComponent(email)}`);
  }

  /**
   * Recherche les réservations par statut
   * MCP Tool: rechercher_reservations_par_statut
   */
  searchReservationsByStatut(statut: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations/statut/${statut}`);
  }

  /**
   * Récupère les détails d'une réservation
   * MCP Tool: details_reservation
   */
  getReservationDetails(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/reservations/${id}`);
  }

  /**
   * Récupère les réservations d'une location
   * MCP Tool: reservations_par_location
   */
  searchReservationsByLocation(locationId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations/location/${locationId}`);
  }

  /**
   * ==================== AUTH ====================
   */

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/auth/login`, { email, password });
  }

  logout(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/auth/logout`, {});
  }

  /**
   * ==================== CHAT ====================
   */

  sendChatMessage(sessionId: string, message: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/chat/${sessionId}`, { message });
  }
}
