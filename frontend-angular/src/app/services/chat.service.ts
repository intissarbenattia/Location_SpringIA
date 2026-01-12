import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { ChatMessage } from '../models/chat.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  // URL de l'agent IA (via gateway)
  private apiUrl = 'http://localhost:8888/api/agent';

  // BehaviorSubject pour gérer l'historique des messages
  private messagesSubject = new BehaviorSubject<ChatMessage[]>([]);
  public messages$ = this.messagesSubject.asObservable();

  // ID de la session courante
  private currentSessionId: string | null = null;

  constructor(private http: HttpClient) {
    this.currentSessionId = localStorage.getItem('sessionId');
  }

  /**
   * Récupère l'historique des messages
   */
  public getMessages(): ChatMessage[] {
    return this.messagesSubject.value;
  }

  /**
   * Ajoute un message à l'historique
   */
  public addMessage(message: ChatMessage): void {
    const messages = [...this.messagesSubject.value, message];
    this.messagesSubject.next(messages);
  }

  /**
   * Efface l'historique des messages
   */
  public clearMessages(): void {
    this.messagesSubject.next([]);
  }

  /**
   * Envoie un message à l'agent IA (sans streaming)
   * @param message Contenu du message
   * @returns Observable de la réponse du chat
   */
  sendMessage(message: string): Observable<any> {
    const request = {
      message: message,
      sessionId: this.currentSessionId || undefined
    };

    console.log('📤 Envoi du message:', request);
    return this.http.post<any>(`${this.apiUrl}/chat`, request);
  }

  /**
   * Définit l'ID de la session courante
   */
  public setSessionId(id: string): void {
    this.currentSessionId = id;
    localStorage.setItem('sessionId', id);
    console.log('💾 Session ID sauvegardé:', id);
  }

  /**
   * Récupère l'ID de la session courante
   */
  public getSessionId(): string | null {
    return this.currentSessionId;
  }

  /**
   * Efface la session courante
   */
  public clearSession(): Observable<any> {
    if (this.currentSessionId) {
      const id = this.currentSessionId;
      this.currentSessionId = null;
      localStorage.removeItem('sessionId');
      this.clearMessages();

      console.log('🗑️ Suppression de la session:', id);
      return this.http.delete<any>(`${this.apiUrl}/session/${id}`);
    }

    this.clearMessages();
    return new Observable(observer => {
      observer.next(undefined);
      observer.complete();
    });
  }
}