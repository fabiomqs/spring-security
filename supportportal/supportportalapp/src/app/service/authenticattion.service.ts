import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';

import {environment} from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({providedIn: 'root'})
export class AuthenticattionService {

  private host = environment.apiUrl;
  private loginPath = environment.loginPath;

  constructor(private httpClient:HttpClient) { }

  public login(user:User):Observable<HttpResponse<any> | HttpErrorResponse> {
      return this.httpClient.post<HttpResponse<any> | HttpErrorResponse>(
        `${this.host}${this.loginPath}`, user, {observe: 'response'});
  }
}
