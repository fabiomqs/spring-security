import { HttpClient, HttpResponse } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/model/user';
import { APIURL } from '../tokens';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    constructor(
        private httpClient:HttpClient,
        @Inject(APIURL) private apiUrl: string
    ) { }

    authenticate(user: User): Observable<HttpResponse<User>> {
        return this.httpClient.post<User>(
            `${this.apiUrl}/login`, user, {observe: 'response', withCredentials: true});
    }

    

}
