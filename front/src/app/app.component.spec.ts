import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';


describe('AppComponent', () => {
  let component : AppComponent;
  let fixture : ComponentFixture<AppComponent>;
  let router : Router;

  @Component({
    selector: 'app-login',
    template: '',
  })
  class MockLoginComponent{}

  @Component({
    selector: 'app-register',
    template: '',
  })
  class MockRegisterComponent{}

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          {path:'login', component: MockLoginComponent},
          {path: 'register', component : MockRegisterComponent},
        ]),
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create the app', () => {
    let fixture = TestBed.createComponent(AppComponent);
    let app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('Redirection vers la page de login',(async () => {
    let toolbar = fixture.debugElement.queryAll(
      By.css('span')
    );
    toolbar[1].nativeElement.click();
    fixture.detectChanges();
    expect(router.url).toBe('/login');
  }));


  it('Redirection vers la page register',(async () => {
    let toolbar = fixture.debugElement.queryAll(
      By.css('span')
    );
    toolbar[2].nativeElement.click();
    fixture.detectChanges();
    expect(router.url).toBe('/register');
  }));

  it('Deconection',(async () => {
    let logoutSpy = jest.spyOn(component, 'logout');
    jest.spyOn(component, '$isLogged').mockReturnValue(of(true));
    fixture.detectChanges();
    let toolbar = fixture.debugElement.queryAll(By.css('span'));

    toolbar[3].nativeElement.click();
    fixture.detectChanges();
    expect(router.url).toBe('/');
    expect(logoutSpy).toHaveBeenCalled();
  }));
});
