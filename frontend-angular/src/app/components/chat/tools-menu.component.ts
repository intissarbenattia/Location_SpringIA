import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Tool {
  id: string;
  name: string;
  description: string;
  category: 'location' | 'reservation';
  icon: string;
  example?: string;
}

@Component({
  selector: 'app-tools-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tools-menu.component.html',
  styleUrl: './tools-menu.component.scss'
})
export class ToolsMenuComponent implements OnInit {
  @Output() close = new EventEmitter<void>();
  @Output() toolSelected = new EventEmitter<string>();

  locationTools: Tool[] = [
    {
      id: 'rechercher_locations',
      name: 'Toutes les locations',
      description: 'Récupère la liste complète de toutes les locations disponibles',
      category: 'location',
      icon: 'bi-house-door',
      example: 'Montre-moi toutes les locations'
    },
    {
      id: 'rechercher_locations_disponibles',
      name: 'Locations disponibles',
      description: 'Affiche uniquement les locations actuellement disponibles',
      category: 'location',
      icon: 'bi-check-circle',
      example: 'Quelles locations sont disponibles ?'
    },
    {
      id: 'rechercher_appartements_disponibles',
      name: 'Appartements disponibles',
      description: 'Recherche les appartements disponibles immédiatement',
      category: 'location',
      icon: 'bi-building',
      example: 'Quels appartements sont disponibles ?'
    },
    {
      id: 'rechercher_maisons_disponibles',
      name: 'Maisons disponibles',
      description: 'Recherche les maisons disponibles pour une réservation',
      category: 'location',
      icon: 'bi-house',
      example: 'Montre-moi les maisons disponibles'
    },
    {
      id: 'rechercher_studios_disponibles',
      name: 'Studios disponibles',
      description: 'Trouve des studios disponibles pour location courte durée',
      category: 'location',
      icon: 'bi-building-fill',
      example: 'Quels studios sont disponibles ?'
    },
    {
      id: 'rechercher_voitures_disponibles',
      name: 'Voitures disponibles',
      description: 'Affiche toutes les voitures de location disponibles',
      category: 'location',
      icon: 'bi-car-front',
      example: 'Montre-moi les voitures disponibles'
    },
    {
      id: 'rechercher_par_type',
      name: 'Recherche par type',
      description: 'Trouve des locations selon leur type (Appartement, Maison, Voiture, etc.)',
      category: 'location',
      icon: 'bi-search',
      example: 'Quels appartements avez-vous ?'
    },
    {
      id: 'rechercher_disponibles_par_type',
      name: 'Type disponible',
      description: 'Recherche des locations disponibles d\'un type spécifique',
      category: 'location',
      icon: 'bi-filter',
      example: 'Donne-moi les studios disponibles'
    },
    {
      id: 'details_location',
      name: 'Détails location',
      description: 'Affiche les informations complètes d\'une location spécifique',
      category: 'location',
      icon: 'bi-info-circle',
      example: 'Donne-moi les détails de la location 5'
    },
    {
      id: 'calculer_prix_location',
      name: 'Calcul de prix',
      description: 'Calcule le prix total d\'une location pour un nombre de jours',
      category: 'location',
      icon: 'bi-calculator',
      example: 'Combien coûte la location 3 pour 5 jours ?'
    }
  ];

  reservationTools: Tool[] = [
    {
      id: 'rechercher_reservations',
      name: 'Toutes les réservations',
      description: 'Récupère la liste complète de toutes les réservations',
      category: 'reservation',
      icon: 'bi-calendar-check',
      example: 'Affiche toutes les réservations'
    },
    {
      id: 'rechercher_reservations_confirmees',
      name: 'Réservations confirmées',
      description: 'Affiche uniquement les réservations avec statut CONFIRMÉE',
      category: 'reservation',
      icon: 'bi-check-all',
      example: 'Quelles réservations sont confirmées ?'
    },
    {
      id: 'rechercher_reservations_client',
      name: 'Réservations client',
      description: 'Retrouve toutes les réservations d\'un client par email',
      category: 'reservation',
      icon: 'bi-person',
      example: 'Montre-moi les réservations de client@email.com'
    },
    {
      id: 'details_reservation',
      name: 'Détails réservation',
      description: 'Affiche les informations complètes d\'une réservation',
      category: 'reservation',
      icon: 'bi-file-text',
      example: 'Donne-moi les détails de la réservation 3'
    },
    {
      id: 'reservations_par_location',
      name: 'Réservations par location',
      description: 'Affiche tous les réservations associées à une location',
      category: 'reservation',
      icon: 'bi-link',
      example: 'Quelles réservations pour la location 5 ?'
    },
    {
      id: 'rechercher_reservations_par_statut',
      name: 'Réservations par statut',
      description: 'Recherche des réservations selon leur statut',
      category: 'reservation',
      icon: 'bi-list-check',
      example: 'Montre-moi les réservations en attente'
    }
  ];

  allTools: Tool[] = [];
  selectedCategory: 'all' | 'location' | 'reservation' = 'all';

  ngOnInit(): void {
    this.updateTools();
  }

  updateTools(): void {
    if (this.selectedCategory === 'all') {
      this.allTools = [...this.locationTools, ...this.reservationTools];
    } else if (this.selectedCategory === 'location') {
      this.allTools = this.locationTools;
    } else {
      this.allTools = this.reservationTools;
    }
  }

  setCategory(category: 'all' | 'location' | 'reservation'): void {
    this.selectedCategory = category;
    this.updateTools();
  }

  onClose(): void {
    this.close.emit();
  }

  useTool(toolId: string): void {
    console.log(`Utilisation de l'outil: ${toolId}`);
    this.toolSelected.emit(toolId);
    this.onClose();
  }

  getCategoryColor(category: string): string {
    switch(category) {
      case 'location': return '#667eea';
      case 'reservation': return '#764ba2';
      default: return '#6c757d';
    }
  }

  getCategoryIcon(category: string): string {
    switch(category) {
      case 'location': return 'bi-house-door';
      case 'reservation': return 'bi-calendar-check';
      default: return 'bi-grid';
    }
  }
}