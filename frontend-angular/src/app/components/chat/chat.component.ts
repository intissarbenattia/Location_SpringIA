import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ChatService } from '../../services/chat.service';
import { AuthService } from '../../services/auth.service';
import { ChatMessage } from '../../models/chat.model';
import { ToolsMenuComponent } from './tools-menu.component';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, ToolsMenuComponent],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: ChatMessage[] = [];
  userMessage = '';
  username = '';
  loading = false;
  shouldScroll = false;
  showToolsMenu = false;
  showToolSuggestions = false;

  exampleQuestions = [
    "Quels appartements sont disponibles ?",
    "Montre-moi les voitures de location",
    "Quel est le prix du studio centre-ville ?",
    "Y a-t-il des réservations en cours ?"
  ];

  constructor(
    private chatService: ChatService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = localStorage.getItem('username') || 'Utilisateur';

    this.chatService.messages$.subscribe(messages => {
      this.messages = messages;
      this.shouldScroll = true;
    });
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage(message?: string): void {
    const messageToSend = message || this.userMessage.trim();

    if (!messageToSend) return;

    console.log('📨 Envoi du message:', messageToSend);

    const userMsg: ChatMessage = {
      role: 'user',
      content: messageToSend,
      timestamp: new Date()
    };
    this.chatService.addMessage(userMsg);

    this.userMessage = '';
    this.loading = true;

    const assistantMsg: ChatMessage = {
      role: 'assistant',
      content: 'En train de réfléchir...',
      timestamp: new Date()
    };
    this.chatService.addMessage(assistantMsg);

    this.chatService.sendMessage(messageToSend).subscribe({
      next: (response: any) => {
        console.log('📥 Réponse reçue:', response);

        if (response && response.response) {
          this.typewriterEffect(assistantMsg, response.response);
          
          if (response.sessionId) {
            this.chatService.setSessionId(response.sessionId);
          }
        } else {
          console.error('❌ Structure de réponse invalide:', response);
          assistantMsg.content = '❌ Erreur: Réponse invalide du serveur';
          this.messages = [...this.messages];
          this.loading = false;
        }
      },
      error: (error: any) => {
        console.error('❌ Erreur complète:', error);
        
        let errorMessage = '❌ Désolé, une erreur est survenue.';
        
        if (error.status === 0) {
          errorMessage = '❌ Impossible de contacter le serveur. Vérifiez que le backend est démarré.';
        } else if (error.message && error.message.includes('Read timed out')) {
          errorMessage = '⏱️ Ollama prend trop de temps à répondre. Vérifiez que Ollama est lancé avec: ollama serve';
        } else if (error.error && error.error.error) {
          errorMessage = `❌ Erreur: ${error.error.error}`;
        } else if (error.message) {
          errorMessage = `❌ Erreur: ${error.message}`;
        }

        assistantMsg.content = errorMessage;
        this.messages = [...this.messages];
        this.loading = false;
      }
    });
  }

  useExample(question: string): void {
    this.userMessage = question;
    this.sendMessage();
  }

  toggleToolsMenu(): void {
    this.showToolsMenu = !this.showToolsMenu;
  }

  closeToolsMenu(): void {
    this.showToolsMenu = false;
  }

  filterTools(): void {
    const message = this.userMessage.toLowerCase();
    
    if (message.length > 2) {
      const keywords = ['location', 'appartement', 'maison', 'studio', 'voiture', 'réservation', 'reservation', 'prix', 'disponible'];
      this.showToolSuggestions = keywords.some(k => message.includes(k));
    } else {
      this.showToolSuggestions = false;
    }
  }

  newSession(): void {
    if (confirm('Voulez-vous démarrer une nouvelle session ?')) {
      this.chatService.clearSession().subscribe({
        next: () => {
          console.log('✅ Nouvelle session créée');
        },
        error: (error: any) => {
          console.error('❌ Erreur lors de la suppression de la session', error);
        }
      });
    }
  }

  logout(): void {
    if (confirm('Voulez-vous vraiment vous déconnecter ?')) {
      this.authService.logout();
    }
  }

  private scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    } catch(err) {
      console.error('Erreur de scroll', err);
    }
  }

  private typewriterEffect(message: ChatMessage, fullText: string, speed: number = 10): void {
    let currentIndex = 0;
    message.content = '';

    const typeNextCharacter = () => {
      if (currentIndex < fullText.length) {
        message.content += fullText[currentIndex];
        currentIndex++;
        
        this.messages = [...this.messages];
        this.shouldScroll = true;
        
        setTimeout(typeNextCharacter, speed);
      } else {
        this.loading = false;
        this.shouldScroll = false;
      }
    };

    typeNextCharacter();
  }

  formatTime(date: Date): string {
    return new Date(date).toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}