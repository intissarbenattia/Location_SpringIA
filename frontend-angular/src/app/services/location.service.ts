import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Location } from '../models/location.model';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  private apiUrl = 'http://localhost:8888/api/locations';

  constructor(private http: HttpClient) {
    console.log('🔧 LocationService initialisé - API URL:', this.apiUrl);
  }

  getAllLocations(): Observable<Location[]> {
    console.log('📡 Récupération de toutes les locations');
    return this.http.get<Location[]>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getLocationById(id: number): Observable<Location> {
    console.log('📡 Récupération de la location:', id);
    return this.http.get<Location>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  createLocation(location: Location): Observable<Location> {
    console.log('📡 Création d\'une nouvelle location:', location);
    return this.http.post<Location>(this.apiUrl, location).pipe(
      catchError(this.handleError)
    );
  }

  updateLocation(id: number, location: Location): Observable<Location> {
    console.log('📡 Mise à jour de la location:', id, location);
    return this.http.put<Location>(`${this.apiUrl}/${id}`, location).pipe(
      catchError(this.handleError)
    );
  }

  deleteLocation(id: number): Observable<void> {
    console.log('📡 Suppression de la location:', id);
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('❌ Erreur LocationService:', {
      status: error.status,
      message: error.message,
      url: error.url
    });
    return throwError(() => error);
  }
}