import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';

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

    public getUsers():Observable<User[] | HttpErrorResponse> {
        return this.httpClient.get<User[]>(
            `${this.host}${this.prefix}${this.user}/list`);
    }

    public addUser(formData:FormData):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.prefix}${this.user}/add`, 
            formData);
    }

    public updateUser(formData:FormData):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.prefix}${this.user}/update`, 
            formData);
    }

    public resetpassword(email:string):Observable<any | HttpErrorResponse> {
        return this.httpClient.get(
            `${this.host}${this.prefix}${this.user}/resetpassword/${email}`);
    }

    public register(user:User):Observable<User | HttpErrorResponse> {
        return this.httpClient.post<User>(
            `${this.host}${this.prefix}${this.user}/register`, 
            user);
    }
}
