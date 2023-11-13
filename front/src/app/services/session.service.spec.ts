import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  let sessionInformation : SessionInformation = {
    token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE2Nzg5NjQwNjQsImV4cCI6MTY3OTA1MDQ2NH0.ZHTp0XHK6IaWLzTKLHwyJXeDOAsyu9RPL',
    type: 'Bearer',
    id: 1,
    username: 'user@yopmail.com',
    firstName: 'User',
    lastName: 'user',
    admin: true
  }

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('A la connexion on charge les infos du user dans UserInformation', ()=>{
    service.logIn(sessionInformation);
    expect(service.sessionInformation).toBe(sessionInformation);
    expect(service.isLogged).toBeTruthy();
  });

  it('Effacer les infos à la deconnection', ()=>{
    service.logOut();
    expect(service.sessionInformation).toBe(undefined);
    expect(service.isLogged).toBeFalsy();
  })
});
