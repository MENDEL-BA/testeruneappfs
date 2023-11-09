import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { By } from '@angular/platform-browser';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService : AuthService;
  let router : Router;
  let fb : FormBuilder;
  let sessionService: SessionService;

  let registerRequest: RegisterRequest = {
    email: 'user@yopmail.com',
    firstName: 'userName',
    lastName : 'lastName',
    password : 'passer'
  }

  let badRegisterRequest: RegisterRequest = {
    email: 'user@@yopmail.com',
    firstName: '',
    lastName : '',
    password : 'pa'
  }


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService,
        { 
          provide: 
            Router, 
            useValue: { 
              navigate: jest.fn()
             } 
        },
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fb = TestBed.inject(FormBuilder);
    sessionService = TestBed.inject(SessionService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Button submit is enabled if form control is valid', fakeAsync(async ()=>{
    let email = component.form.controls['email'];
    let firstname = component.form.controls['firstName'];
    let lastName = component.form.controls['lastName'];
    let password = component.form.controls['password'];
    email.setValue(registerRequest.email);
    firstname.setValue(registerRequest.firstName);
    lastName.setValue(registerRequest.lastName);
    password.setValue(registerRequest.password);

    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(component.form.valid).toBeTruthy();
    expect(button.nativeElement.enabled).toBeFalsy();
  }));

  it('Button submit is disabled if form control is not valid', fakeAsync(async ()=>{
    let email = component.form.controls['email'];
    let firstname = component.form.controls['firstName'];
    let lastName = component.form.controls['lastName'];
    let password = component.form.controls['password'];
    email.setValue(badRegisterRequest.email);
    firstname.setValue(badRegisterRequest.firstName);
    lastName.setValue(badRegisterRequest.lastName);
    password.setValue(badRegisterRequest.password);

    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(component.form.valid).toBeFalsy();
    expect(button.nativeElement.enabled).toBeFalsy();
  }));

  it('Error message is shown when the register request is not valid', async ()=>{
    let routerSpy = jest.spyOn(router, 'navigate');
    jest.spyOn(authService, 'register').mockReturnValue(throwError(()=>new Error()));
    authService.register(badRegisterRequest).subscribe();
    fixture.debugElement.query(By.css('.register-form'))
      .triggerEventHandler('submit', undefined);

    fixture.detectChanges();

    let errorElement = fixture.debugElement.query(By.css('.error')).nativeElement;
    expect(errorElement).toBeTruthy();
    expect(routerSpy).not.toBeCalled();
  });
  test('Redirect to login page if request is valid ', async () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    jest.spyOn(sessionService, 'logIn');
    jest.spyOn(component, 'submit');

    authService.register(registerRequest).subscribe();

    fixture.debugElement.query(By.css('.register-form'))
      .triggerEventHandler('submit', undefined);

    fixture.detectChanges();

    expect(component.submit).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
  
});
