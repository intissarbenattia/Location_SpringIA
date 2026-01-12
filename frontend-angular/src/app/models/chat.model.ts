export interface ChatMessage {
  role: 'user' | 'assistant';
  content: string;
  timestamp: Date;
}

/**
 * Modèle pour la requête de chat
 */
export interface ChatRequest {
  message: string;
  conversationId?: string;
}

/**
 * Modèle pour la réponse du chat
 */
export interface ChatResponse {
  response: string;
  conversationId: string;
}

/**
 * Modèle pour l'historique d'une conversation
 */
export interface Conversation {
  id: string;
  messages: ChatMessage[];
  createdAt: Date;
  updatedAt: Date;
}
