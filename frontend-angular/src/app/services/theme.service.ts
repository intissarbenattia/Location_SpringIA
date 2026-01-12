// src/app/services/theme.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private themeSubject: BehaviorSubject<string>;
  public theme$: Observable<string>;

  constructor() {
    // Récupérer le thème depuis localStorage ou utiliser 'light' par défaut
    const savedTheme = localStorage.getItem('theme') || 'light';
    this.themeSubject = new BehaviorSubject<string>(savedTheme);
    this.theme$ = this.themeSubject.asObservable();
    
    // Appliquer le thème initial
    this.applyTheme(savedTheme);
  }

  /**
   * Obtenir le thème actuel
   */
  getCurrentTheme(): string {
    return this.themeSubject.value;
  }

  /**
   * Changer le thème
   */
  setTheme(theme: string): void {
    this.themeSubject.next(theme);
    this.applyTheme(theme);
    localStorage.setItem('theme', theme);
  }

  /**
   * Basculer entre light et dark
   */
  toggleTheme(): void {
    const newTheme = this.getCurrentTheme() === 'light' ? 'dark' : 'light';
    this.setTheme(newTheme);
  }

  /**
   * Appliquer le thème au document
   */
  private applyTheme(theme: string): void {
    document.documentElement.setAttribute('data-theme', theme);
  }
}