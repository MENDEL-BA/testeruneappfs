import {AuthService} from "./auth.service";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {TestBed} from "@angular/core/testing";
import { expect } from '@jest/globals';
import {HttpClient, HttpClientModule} from "@angular/common/http";

describe('AuthService', () => {
  let authService: AuthService;
  let httpClient: HttpClient;

  let requestRegister: RegisterRequest = {
    email: 'user@yopmail.com',
    firstName: 'firstName',
    lastName: 'lastName',
    password: 'passer'
  };

  let requestLogin: LoginRequest = {
    email: 'user@yopmail.com',
    password: 'passer'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [{
        provide: HttpClient, useValue: { post: jest.fn()}
      },],
    });
    authService = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should create', () => {
    expect(authService).toBeTruthy();
  });

  it('Request post for register ', ()=>{
    authService.register(requestRegister);
    expect(httpClient.post).toHaveBeenCalledWith('api/auth/register', requestRegister);
  });

  it('Request login ', ()=>{
    authService.login(requestLogin);
    expect(httpClient.post).toHaveBeenCalledWith('api/auth/login', requestLogin);
  });

});