import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/auth.service';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, MatCardModule, MatButtonModule],
    templateUrl: './dashboard.component.html',
    styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected metrics = [
    { label: 'Agendamentos', value: '128' },
    { label: 'Cancelamentos', value: '17' },
    { label: 'Taxa Ocupação', value: '82%' },
    { label: 'Recorrentes', value: '46' }
  ];

  protected logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }
}
