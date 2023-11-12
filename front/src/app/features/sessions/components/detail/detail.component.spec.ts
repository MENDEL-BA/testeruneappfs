import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { TeacherService } from 'src/app/services/teacher.service';
import { User } from 'src/app/interfaces/user.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs/internal/observable/of';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

let user: User = {
  id: 1,
  email: 'test@test.com',
  lastName: 'User',
  firstName: 'User',
  admin: false,
  password: 'passer',
  createdAt: new Date(),
};

let user2: User = {
  id: 4,
  email: 'test@yopmail.com',
  lastName: 'Admin',
  firstName: 'Admin',
  admin: false,
  password: 'adminPass',
  createdAt: new Date(),
};

let teacher: Teacher = {
  id: 1,
  lastName: 'John',
  firstName: 'Doe',
  createdAt: new Date(),
  updatedAt: new Date(),
};

let session: Session = {
  id: 1,
  name: 'Session',
  description: 'Description',
  date: new Date(),
  teacher_id: 1,
  users: [1, 2],
  createdAt: new Date(),
  updatedAt: new Date(),
};
describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService,
        useValue: {
          sessionInformation: user},
      },
      { 
        provide: SessionApiService,
        useValue: {
          sessionId: jest.fn().mockReturnValue('1'),
          detail: jest.fn().mockReturnValue(of(session)),
          participate: jest.fn(() => of(null)),
          unParticipate: jest.fn(() => of(null))},
      },
      {
         provide: MatSnackBar,
        useValue: {
          open: jest.fn().mockReturnValue(undefined)},
      },
      { 
        provide: TeacherService,
        useValue: {
          detail: jest.fn().mockReturnValue(of(teacher))},
      },],
    })
      .compileComponents();
    sessionApiService  = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    jest.resetAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Test unParticipate method', (async() => {
    let participateSpy = jest.spyOn(sessionApiService, 'unParticipate');
    fixture.detectChanges();
    let buttonParticipate = fixture.debugElement.query(
      By.css('button[mat-raised-button]')
    );
    expect(buttonParticipate.nativeElement.textContent).toBe(
      'person_removeDo not participate'
    );
    buttonParticipate.triggerEventHandler('click', null);
    fixture.detectChanges();
    expect(participateSpy).toHaveBeenCalled();
  }));

});

describe('DetailComponent-Participate', () => {
  let fixture: ComponentFixture<DetailComponent>; 
  let sessionApiService: SessionApiService;
  
beforeEach(async () => {
  await TestBed.configureTestingModule({
    imports: [
      RouterTestingModule,
      BrowserAnimationsModule,
      HttpClientModule,
      MatSnackBarModule,
      ReactiveFormsModule,
      MatSnackBarModule,
      HttpClientModule,
      MatCardModule,
      MatIconModule,
    ],
    declarations: [DetailComponent], 
    providers: [
      { provide: SessionService,
      useValue: {
        sessionInformation: user2},
    },
    { 
      provide: SessionApiService,
      useValue: {
        sessionId: jest.fn().mockReturnValue('1'),
        detail: jest.fn().mockReturnValue(of(session)),
        participate: jest.fn(() => of(null)),
        unParticipate: jest.fn(() => of(null))},
    },
    {
       provide: MatSnackBar,
      useValue: {
        open: jest.fn().mockReturnValue(undefined)},
    },
    { 
      provide: TeacherService,
      useValue: {
        detail: jest.fn().mockReturnValue(of(teacher))},
    },],
  })
    .compileComponents();
  sessionApiService  = TestBed.inject(SessionApiService);
  fixture = TestBed.createComponent(DetailComponent);
  fixture.detectChanges();
  jest.resetAllMocks();
});
test('Participate method should be called when user clicked on an participate button', async () => {
  let participateSpy = jest.spyOn(sessionApiService, 'participate');
  fixture.detectChanges();
  let participateButton = fixture.debugElement.query(
    By.css('button[mat-raised-button]')
  );
  expect(participateButton.nativeElement.textContent).toBe(
    'person_addParticipate'
  );
  participateButton.triggerEventHandler('click', null);
  fixture.detectChanges();
  expect(participateSpy).toHaveBeenCalled();
});
});