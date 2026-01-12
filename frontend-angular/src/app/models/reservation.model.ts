export interface Reservation {
  id?: number;
  locationId: number;
  locationNom?: string;
  clientNom: string;
  clientEmail: string;
  dateDebut: string;
  dateFin: string;
  statut: 'CONFIRMEE' | 'EN_ATTENTE' | 'ANNULEE';
  prixTotal: number;
}