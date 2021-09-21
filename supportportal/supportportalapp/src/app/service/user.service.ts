import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import {environment} from '../../environments/environment';

import { User } from '../model/user';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private host = environment.apiUrl;
  private prefix = environment.prefix;
  private user = environment.user;

  constructor(private httpClient:HttpClient) { }

  public register(user:User):Observable<User | HttpErrorResponse> {
    return this.httpClient.post<User | HttpErrorResponse>(
      `${this.host}${this.prefix}${this.user}/register`, 
      user);
  }
}
