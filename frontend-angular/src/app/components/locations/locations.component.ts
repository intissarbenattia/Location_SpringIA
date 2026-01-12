import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Location } from '../../models/location.model';
import { LocationService } from '../../services/location.service';

@Component({
  selector: 'app-locations',
  imports: [CommonModule, FormsModule],
  templateUrl: './locations.component.html',
  styleUrl: './locations.component.scss'
})
export class LocationsComponent implements OnInit {
  locations: Location[] = [];
  loading = false;
  error: string | null = null;

  // Pour les formulaires
  showCreateForm = false;
  showEditForm = false;
  editingLocation: Location | null = null;

  newLocation: Location = {
    type: '',
    nom: '',
    adresse: '',
    prixParJour: 0,
    disponible: true,
    description: ''
  };

  constructor(private locationService: LocationService) {}

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.loading = true;
    this.error = null;
    this.locationService.getAllLocations().subscribe({
      next: (data) => {
        this.locations = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des locations';
        this.loading = false;
        console.error('Erreur chargement locations:', err);
      }
    });
  }

  createLocation(): void {
    if (!this.isValidLocation(this.newLocation)) {
      this.error = 'Veuillez remplir tous les champs obligatoires';
      return;
    }

    this.loading = true;
    this.locationService.createLocation(this.newLocation).subscribe({
      next: (location) => {
        this.locations.push(location);
        this.resetNewLocation();
        this.showCreateForm = false;
        this.loading = false;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la création de la location';
        this.loading = false;
        console.error('Erreur création location:', err);
      }
    });
  }

  editLocation(location: Location): void {
    this.editingLocation = { ...location };
    this.showEditForm = true;
    this.showCreateForm = false;
  }

  updateLocation(): void {
    if (!this.editingLocation || !this.isValidLocation(this.editingLocation)) {
      this.error = 'Veuillez remplir tous les champs obligatoires';
      return;
    }

    this.loading = true;
    this.locationService.updateLocation(this.editingLocation.id!, this.editingLocation).subscribe({
      next: (updatedLocation) => {
        const index = this.locations.findIndex(l => l.id === updatedLocation.id);
        if (index !== -1) {
          this.locations[index] = updatedLocation;
        }
        this.cancelEdit();
        this.loading = false;
        this.error = null;
      },
      error: (err) => {
        this.error = 'Erreur lors de la mise à jour de la location';
        this.loading = false;
        console.error('Erreur mise à jour location:', err);
      }
    });
  }

  deleteLocation(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette location ?')) {
      this.loading = true;
      this.locationService.deleteLocation(id).subscribe({
        next: () => {
          this.locations = this.locations.filter(l => l.id !== id);
          this.loading = false;
          this.error = null;
        },
        error: (err) => {
          this.error = 'Erreur lors de la suppression de la location';
          this.loading = false;
          console.error('Erreur suppression location:', err);
        }
      });
    }
  }

  cancelEdit(): void {
    this.showEditForm = false;
    this.editingLocation = null;
  }

  resetNewLocation(): void {
    this.newLocation = {
      type: '',
      nom: '',
      adresse: '',
      prixParJour: 0,
      disponible: true,
      description: ''
    };
  }

  private isValidLocation(location: Location): boolean {
    return !!(location.type && location.nom && location.adresse && location.prixParJour > 0);
  }
}
