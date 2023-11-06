import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs/internal/observable/of';
import { throwError } from 'rxjs/internal/observable/throwError';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService : AuthService;
  let fb : FormBuilder;
  let router : Router;
  let serviceSession : SessionService;

  let loginRequest: LoginRequest = {
    email: 'user@yopmail.com',
    password: 'userPass'
  }

  let badLoginRequest: LoginRequest = {
    email: '',
    password: ''
  }

  let responseSession : SessionInformation = {
    token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2Nzg5NjQwNjQsImV4cCI6MTY3OTA1MDQ2NH0.ZHTp0XHK6IaWLzTKLHwyJXeDOAsyu9RPLgUBUjaniDulLLttRlWP-nVfZCpjIqn6Zb-xAumtPPLVxR9L2L17gA',
    type: 'Bearer',
    id: 1,
    username: 'user@yopmail.com',
    firstName: 'user',
    lastName: 'userlast',
    admin: true,
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        AuthService,
        { 
          provide: 
            Router, 
            useValue: { 
              navigate: jest.fn()
             } 
        },
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    fb = TestBed.inject(FormBuilder);
    router = TestBed.inject(Router);
    serviceSession = TestBed.inject(SessionService);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('Button submit is disable if password not present ', ()=>{
    let button = fixture.debugElement.query(By.css('button[type="submit"]'));
    fixture.detectChanges;
    expect(button.nativeElement.disabled).toBeTruthy();
  })

  test('Button submit is disable if email not present ', ()=>{
    let emailInput = component.form.controls['email'];
    emailInput.setValue('');
    fixture.detectChanges;
    expect(emailInput.valid).toBeFalsy();
  })


  test('Submit form with an invalid email ', () =>{
    let formControl = component.form.controls['email'];
    formControl.setValue('user@@yopmail.com');
    fixture.detectChanges();
    expect(formControl.valid).toBeFalsy();
  })

  test('Submit form with a valid email ', () =>{
    let formControl = component.form.controls['email'];
    formControl.setValue('user@yopmail.com');
    fixture.detectChanges();
    expect(formControl.valid).toBeTruthy();
  })

  test('Submit button disable if email and pasword not present ', () =>{
    let formControlEmail = component.form.controls['email'];
    let formControlPassword = component.form.controls['password'];
    formControlEmail.setValue('');
    formControlPassword.setValue('');
    let button = fixture.debugElement.query(By.css('button[type="submit"]'));
    fixture.detectChanges();
    expect(button.nativeElement.disabled).toBeTruthy();
  })

  test('Submit valid Login Request ', () =>{
    let authServiceSpy = jest.spyOn(authService,'login').mockReturnValue(of(responseSession));
    let sessionSpy = jest.spyOn(serviceSession, 'logIn');
    let routerSpy = jest.spyOn(router, 'navigate');
    let buttonSubmitSpy = jest.spyOn(component, 'submit');

    authService.login(loginRequest);
    serviceSession.logIn(responseSession);

    let formControl = fixture.debugElement.query(By.css('.login-form'));
    formControl.triggerEventHandler('submit', null);

    fixture.detectChanges();

    expect(component.submit).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);

  })

  test('Submit an invalid Login Request ', () =>{
    let authServiceSpy = jest.spyOn(authService,'login').mockReturnValue(throwError(() => new Error('')));
    let routerSpy = jest.spyOn(router, 'navigate');

    authService.login(badLoginRequest).subscribe();

    let formControl = fixture.debugElement.query(By.css('.login-form'));
    formControl.triggerEventHandler('submit', null);

    fixture.detectChanges();
    let formControlError = fixture.debugElement.query(By.css('.error'));
    
    expect(formControlError.nativeElement).toBeTruthy();
    expect(routerSpy).not.toBeCalled();

  })
});
