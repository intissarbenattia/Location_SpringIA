export interface Location {
  id?: number;
  type: string;
  nom: string;
  adresse: string;
  prixParJour: number;
  disponible: boolean;
  description?: string;
}