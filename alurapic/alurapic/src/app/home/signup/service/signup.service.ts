import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { APIURL } from 'src/app/core/tokens';
import { NewUser } from 'src/app/model/new-user';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

    constructor(
        private httpClient:HttpClient,
        @Inject(APIURL) private apiUrl: string
    ) { }

    
    checkUsernameTaken(username: string) {
        return this.httpClient.get(this.apiUrl + '/api/v1/user/exists/' + username);
    }

    signup(newUser:NewUser) {
        const formData = new FormData();
        formData.append('firstName', newUser.firstName);
        formData.append('lastName', newUser.lastName);
        formData.append('username', newUser.username);
        formData.append('password', newUser.password);
        formData.append('email', newUser.email);

        return this.httpClient.post(this.apiUrl + '/api/v1/user/register', formData);
    }
}
