import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Observable, of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessions: Session[] = [
  ];

  const mockSessionInformation: SessionInformation = {
    token: 'ZEZEZE',
    type: 'Z',
    id: 1,
    username: 'Session',
    firstName: 'Session',
    lastName: 'Session',
    admin: true
  };

  const sessionServiceMock = {
    get sessionInformation(): SessionInformation | undefined {
      return mockSessionInformation;
    }
  };

  const sessionApiServiceMock = {
    all(): Observable<Session[]> {
      return of(mockSessions);
    }
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService },
                  { provide: SessionApiService, useValue: sessionApiServiceMock }]
      
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have sessions$', () => {
    component.sessions$.subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });
  });
/*
  it('should have user', () => {
    const user = component.user;
    expect(user).toEqual(mockSessionInformation);
  });*/
});
