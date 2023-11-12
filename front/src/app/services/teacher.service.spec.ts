import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient : HttpClient;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ],
      providers: [
        { provide: HttpClient, useValue: {
            get: jest.fn()
        }}]
    });
    service = TestBed.inject(TeacherService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Return tous les teachers si getAll est appelle', ()=>{
    service.all();
    expect(httpClient.get).toHaveBeenCalledWith('api/teacher');
  });

  it('Return les details de teachers si detail est appelle', ()=>{
    service.detail('1')
    expect(httpClient.get).toHaveBeenCalledWith('api/teacher/1');
  });
});
