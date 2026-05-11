import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { DashboardComponent } from './dashboard.component';
import { AuthService } from '../../core/auth.service';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  const authServiceMock = {
    logout: jasmine.createSpy('logout')
  };
  const routerMock = {
    navigateByUrl: jasmine.createSpy('navigateByUrl')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
