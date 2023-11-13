import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient : HttpClient;
  let session : Session = {
    name: 'Session',
    description: 'Desc Session',
    date: new Date(),
    teacher_id: 1,
    users: [1,2,3]
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers : [{
        provide: HttpClient, useValue: {
          post: jest.fn(),
          get: jest.fn(),
          delete: jest.fn(),
          put: jest.fn()}
      },],
    });
    service = TestBed.inject(SessionApiService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Detail api GET', ()=>{
    service.detail('1');
    expect(httpClient.get).toHaveBeenCalledWith('api/session/1');
  });

  it('Detail api GET ALL', ()=>{
    service.all();
    expect(httpClient.get).toHaveBeenCalledWith('api/session');
  });

  it('supprimer une session api Delete', ()=>{
    service.delete('1');
    expect(httpClient.delete).toHaveBeenCalledWith('api/session/1');
  });

  it('Creation session api POST', ()=>{
    service.create(session);
    expect(httpClient.post).toHaveBeenCalledWith('api/session',session);
  });

  it('Mise à jour session api PUT', ()=>{
    service.update('1', session);
    expect(httpClient.put).toHaveBeenCalledWith('api/session/1', session);
  });

  it('Ajout de participant ', ()=>{
    service.participate('1','1');
    expect(httpClient.post).toHaveBeenCalledWith('api/session/1/participate/1', null);
  });

  it('Enlever un participant ', ()=>{
    service.unParticipate('1','1')
    expect(httpClient.delete).toHaveBeenCalledWith('api/session/1/participate/1');
  });

});
