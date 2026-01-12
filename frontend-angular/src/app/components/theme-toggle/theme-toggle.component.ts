import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  imports: [CommonModule],
  template: `
    <button 
      class="theme-toggle-btn" 
      (click)="toggleTheme()"
      [attr.aria-label]="'Changer vers thème ' + (isDark ? 'clair' : 'sombre')">
      <span class="icon">{{ isDark ? '☀️' : '🌙' }}</span>
    </button>
  `,
  styles: [`
    .theme-toggle-btn {
      position: fixed;
      top: 20px;
      right: 20px;
      width: 50px;
      height: 50px;
      border-radius: 50%;
      background: var(--bg-card);
      border: 2px solid var(--border-light);
      box-shadow: var(--shadow-card);
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      z-index: 1000;
    }

    .theme-toggle-btn:hover {
      transform: scale(1.1);
      box-shadow: var(--shadow-hover);
      border-color: var(--primary-cyan);
    }

    .icon {
      font-size: 24px;
      transition: transform 0.3s ease;
    }

    .theme-toggle-btn:active .icon {
      transform: rotate(180deg);
    }
  `]
})
export class ThemeToggleComponent implements OnInit {
  isDark: boolean = false;

  constructor(private themeService: ThemeService) {}

  ngOnInit(): void {
    this.themeService.theme$.subscribe(theme => {
      this.isDark = theme === 'dark';
    });
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }
}