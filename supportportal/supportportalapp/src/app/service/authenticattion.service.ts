import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';

import { JwtHelperService } from "@auth0/angular-jwt";

import {environment} from '../../environments/environment';

import { User } from '../model/user';

@Injectable({providedIn: 'root'})
export class AuthenticattionService {

  private host = environment.apiUrl;
  private loginPath = environment.loginPath;

  private token: string;
  private loggedInUsername: string;
  private jwtHelper = new JwtHelperService();

  constructor(private httpClient:HttpClient) { }

  public login(user:User):Observable<HttpResponse<any> | HttpErrorResponse> {
      return this.httpClient.post<HttpResponse<any> | HttpErrorResponse>(
        `${this.host}${this.loginPath}`, user, {observe: 'response'});
  }

  public logout(): void{
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }

  public saveToken(token: string):void {
    this.token = token;
    localStorage.setItem('token', token);
  }

  public addUserToLocalCache(user: User):void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  public getUserFromLocalCache():User {
    return JSON.parse(localStorage.getItem('user'));
  }

  public loadToken():void {
    this.token = localStorage.getItem('token');
  }

  public getToken():string {
    return this.token;
  }

  public isLoggedIn():boolean {
      this.loadToken();
      if(this.token != null && this.token != '') {
          if(this.jwtHelper.decodeToken(this.token).sub != null || '') {
              if(!this.jwtHelper.isTokenExpired(this.token)) {
                  this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
                  return true;
              }
          }
          return false;
      } else {
        this.logout();
        return false;
      }
  }

  
}
