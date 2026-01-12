import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  // Modèle du formulaire
  username = '';
  password = '';
  
  // États
  loading = false;
  errorMessage = '';
  showPassword = false;
  
  // URL de retour après connexion
  returnUrl = '/chat';
  
  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Récupérer l'URL de retour depuis les query params
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/chat';
    
    console.log('🔧 LoginComponent initialisé');
    console.log('📍 URL de retour:', this.returnUrl);
  }
  
  /**
   * Gère la soumission du formulaire de connexion
   */
  onLogin(): void {
    console.log('🚀 Début du processus de connexion');
    
    // Validation basique
    if (!this.username.trim() || !this.password.trim()) {
      console.warn('⚠️ Champs vides détectés');
      this.errorMessage = 'Veuillez remplir tous les champs';
      return;
    }
    
    // Réinitialiser les états
    this.loading = true;
    this.errorMessage = '';
    
    console.log('👤 Tentative de connexion pour:', this.username);
    
    // Préparer la requête
    const credentials: LoginRequest = {
      username: this.username,
      password: this.password
    };
    
    console.log('📡 Envoi de la requête au backend...');
    
    // Appeler le service d'authentification
    this.authService.login(credentials).subscribe({
      next: (response) => {
        // Connexion réussie
        console.log('✅ Connexion réussie!', response);
        console.log('🎫 Token reçu:', response.token.substring(0, 20) + '...');
        
        this.loading = false;
        
        // Rediriger vers l'URL de retour
        console.log('🚀 Redirection vers:', this.returnUrl);
        this.router.navigate([this.returnUrl]);
      },
      error: (error) => {
        // Connexion échouée
        console.error('❌ Erreur de connexion complète:', error);
        console.error('📊 Statut HTTP:', error.status);
        console.error('📝 Message:', error.message);
        console.error('🌐 URL:', error.url);
        
        this.loading = false;
        
        // Afficher un message d'erreur approprié
        if (error.status === 401 || error.status === 400) {
          this.errorMessage = 'Identifiants incorrects';
          console.error('🔒 Authentification refusée');
        } else if (error.status === 0) {
          this.errorMessage = 'Impossible de contacter le serveur';
          console.error('🔌 Pas de connexion au serveur');
          console.error('💡 Vérifiez que:');
          console.error('   1. Le Gateway est démarré (port 8888)');
          console.error('   2. Le Auth Service est démarré (port 8080)');
          console.error('   3. CORS est bien configuré');
        } else if (error.status === 404) {
          this.errorMessage = 'Service d\'authentification non trouvé';
          console.error('🔍 Route Gateway incorrecte ou service non enregistré dans Eureka');
        } else {
          this.errorMessage = 'Une erreur est survenue. Veuillez réessayer.';
          console.error('⚠️ Erreur inconnue');
        }
      }
    });
  }
  
  /**
   * Bascule l'affichage du mot de passe
   */
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
    console.log('👁️ Visibilité mot de passe:', this.showPassword ? 'visible' : 'masqué');
  }
}