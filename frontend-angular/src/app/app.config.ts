import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors, withFetch } from '@angular/common/http';
import { routes } from './app.routes';
import { jwtInterceptor } from './interceptors/jwt.interceptor';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { provideAnimations } from '@angular/platform-browser/animations';

// ApexCharts
import { NgApexchartsModule } from 'ng-apexcharts';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),

    provideRouter(routes),

    // ✅ HTTP client avec JWT
    provideHttpClient(
      withInterceptors([jwtInterceptor]),
      withFetch()
    ),

    // ✅ Angular Material
    provideAnimations(),
    importProvidersFrom(MatCardModule),
    importProvidersFrom(MatIconModule),

    // ✅ ApexCharts
    importProvidersFrom(NgApexchartsModule)
  ]
};
