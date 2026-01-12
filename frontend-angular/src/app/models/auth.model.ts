export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * Modèle pour la réponse d'authentification
 */
export interface LoginResponse {
  token: string;
  username: string;
  email: string;
}

/**
 * Modèle pour les informations utilisateur
 */
export interface User {
  username: string;
  email: string;
  roles: string[];
}