import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface AuthRequest {
  email: string;
  password: string;
}

interface AuthResponse {
  accessToken: string;
  tokenType: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/auth';

  login(payload: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, payload).pipe(
      tap(response => localStorage.setItem('bot_agendamento_token', `${response.tokenType} ${response.accessToken}`))
    );
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('bot_agendamento_token');
  }

  logout(): void {
    localStorage.removeItem('bot_agendamento_token');
  }
}
