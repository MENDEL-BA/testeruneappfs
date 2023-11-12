import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [
        { provide: HttpClient, useValue: {
          get: jest.fn(),
            delete: jest.fn(),
        }}
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Return le user si getById est appelle', ()=>{
    service.getById('1');
    expect(httpClient.get).toHaveBeenCalledWith('api/user/1');
  });

  it('Supprime le user si delete est appelle', ()=>{
    service.delete('1');
    expect(httpClient.delete).toHaveBeenCalledWith('api/user/1');
  });
});
