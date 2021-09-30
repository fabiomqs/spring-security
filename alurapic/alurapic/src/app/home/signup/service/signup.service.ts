import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { APIURL } from 'src/app/core/tokens';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

    constructor(
        private httpClient:HttpClient,
        @Inject(APIURL) private apiUrl: string
    ) { }

    ///exists/{username}
    ///api/v1/user/exists/
    checkUsernameTaken(username: string) {
        return this.httpClient.get(this.apiUrl + '/api/v1/user/exists/' + username);
    }

}
