import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;;
  let uservice: UserService;
  let serviceSession: SessionService;
  let bar: MatSnackBar;
  let router: Router;

  let user: User = {
    id: 1,
    email: 'user@user.com',
    lastName: 'User',
    firstName: 'User',
    admin: false,
    password: 'userPass',
    createdAt: new Date('2023-04-01T00:00:00')
  };


  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        {
          provide: SessionService,
          useValue: {
            sessionInformation: user,
            logOut: jest.fn()
          }
        },
        {
          provide: UserService,
          useValue: {
            getById: jest.fn().mockReturnValue(of(user)),
            delete: jest.fn().mockReturnValue(of(user)),
          }
        },
        {
          provide: MatSnackBar,
          useValue: {
            open: jest.fn()
          }
        }
      ],
    })
      .compileComponents();

    uservice = TestBed.inject(UserService);
    serviceSession = TestBed.inject(SessionService);
    bar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    // @ts-ignore
    expect(component).toBeTruthy();
  });

  //Test de suppression de compte
  it('Test delete account with message \'Your account has been deleted ! \' if account is deleted', fakeAsync(async () => {
    let logOutSpy = jest.spyOn(serviceSession, 'logOut');
    let deletedSpy = jest.spyOn(uservice, 'delete');
    let routerSpy = jest.spyOn(router, 'navigate');

    let button = fixture.debugElement.query(
      By.css('button[color="warn"]')
    );

    button.triggerEventHandler('click', null);
    tick(100);
    fixture.detectChanges();
    // @ts-ignore
    expect(logOutSpy).toHaveBeenCalled();
    // @ts-ignore
    expect(deletedSpy).toHaveBeenCalled();
    // @ts-ignore
    expect(bar.open).toHaveBeenCalledWith(
      'Your account has been deleted !', 'Close',
      {duration: 3000}
    );
    // @ts-ignore
    expect(routerSpy).toHaveBeenCalledWith(['/']);
  }));
});
