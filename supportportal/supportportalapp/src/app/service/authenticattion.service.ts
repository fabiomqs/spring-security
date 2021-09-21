import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import {environment} from '../../environments/environment';

import { User } from '../model/user';

@Injectable({providedIn: 'root'})
export class AuthenticattionService {

  private host = environment.apiUrl;
  private loginPath = environment.loginPath;

  private token: string;
  private loggedInUsername: string;

  constructor(private httpClient:HttpClient) { }

  public login(user:User):Observable<HttpResponse<any> | HttpErrorResponse> {
      return this.httpClient.post<HttpResponse<any> | HttpErrorResponse>(
        `${this.host}${this.loginPath}`, user, {observe: 'response'});
  }

  public logout(){
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }

  public saveToken(token: string){
    this.token = token;
    localStorage.setItem('token', token);
  }

  
}
