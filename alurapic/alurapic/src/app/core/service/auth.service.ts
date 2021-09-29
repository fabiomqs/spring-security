import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';

const API = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    constructor(private httpClient:HttpClient) { }

    authenticate(user: User): Observable<HttpResponse<User>> {
        return this.httpClient.post<User>(
            `${API}/login`, user, {observe: 'response', withCredentials: true});
    }

    

}
